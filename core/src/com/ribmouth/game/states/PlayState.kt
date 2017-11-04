package com.ribmouth.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody
import com.ribmouth.game.Game.Companion.HEIGHT
import com.ribmouth.game.Game.Companion.WIDTH
import com.ribmouth.game.entities.Crystal
import com.ribmouth.game.entities.HUD
import com.ribmouth.game.entities.Player
import com.ribmouth.game.handlers.B2DVars.Companion.BIT_BLUE
import com.ribmouth.game.handlers.B2DVars.Companion.BIT_CRYSTAL
import com.ribmouth.game.handlers.B2DVars.Companion.BIT_GREEN
import com.ribmouth.game.handlers.B2DVars.Companion.BIT_PLAYER
import com.ribmouth.game.handlers.B2DVars.Companion.BIT_RED
import com.ribmouth.game.handlers.B2DVars.Companion.PPM
import com.ribmouth.game.handlers.BBInput
import com.ribmouth.game.handlers.BBInput.Companion.BUTTON1
import com.ribmouth.game.handlers.BBInput.Companion.BUTTON2
import com.ribmouth.game.handlers.ContactListener
import com.ribmouth.game.handlers.GameStateManager
import ktx.box2d.chain
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

/**
 * Created by RibMouth on 2/11/2017.
 */
class PlayState(gsm: GameStateManager) : GameState(gsm) {
    private val debug: Boolean = false

    private val world: World = World(Vector2(0f, -9.81f), true)
    private val b2dr: Box2DDebugRenderer = Box2DDebugRenderer()
    private val b2dCam: OrthographicCamera = OrthographicCamera()
    private val contactListener: ContactListener = ContactListener()

    //Map loader and renderer
    private val tileMap: TiledMap = TmxMapLoader().load("maps/level1.tmx")
    private val tileMapRenderer: OrthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(tileMap)
    private var tileSize: Float = 0f

    //Player
    lateinit private var player: Player

    //Crystals
    lateinit private var crystals: MutableList<Crystal>

    //HUD
    private var hud: HUD

    init {
        //Setup box2d stuff
        world.setContactListener(contactListener)

        //Create player
        createPlayer()

        //Create tiles
        createTiles()

        //Create objects
        createCrystals()

        //Create HUD
        hud = HUD(player)

        //Setup cam
        b2dCam.setToOrtho(false, WIDTH / PPM, HEIGHT / PPM)
    }

    override fun handleInput() {
        //Player jump -> W
        if (BBInput.isPressed(BUTTON1)) {
            if (contactListener.playerOnGround) {
                player.body.applyForceToCenter(0f, 250f, true) //Force is in Newtons upwards force
            }
        }

        //Switch block -> X
        if (BBInput.isPressed(BUTTON2)) {
            switchBlocks()
        }
    }

    override fun update(dt: Float) {
        handleInput()

        world.step(dt, 6, 2)

        //Remove bodies
        val bodies = contactListener.bodiesToRemove
        for (body in bodies) {
            crystals.removeIf { c -> c == body.userData as Crystal }
            world.destroyBody(body)
            player.collectCrystal()
        }
        bodies.clear()

        //Update player
        player.update(dt)

        //Update crystals
        for (crystal in crystals) {
            crystal.update(dt)
        }
    }

    override fun render() {
        //Clear screen
        Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        //Set camera to follow player
        cam.position.set(player.position.x * PPM + WIDTH / 4, HEIGHT / 2, 0f)
        cam.update()

        //Draw tileMap
        tileMapRenderer.setView(cam)
        tileMapRenderer.render()

        sb.projectionMatrix = cam.combined
        sb.begin()

        //Render player
        player.render(sb)

        //Render crystals
        for (crystal in crystals) {
            crystal.render(sb)
        }

        //Render HUD
        sb.projectionMatrix = hudCam.combined
        hud.render(sb)

        sb.end()

        //Draw world
        if (debug) b2dr.render(world, b2dCam.combined)
    }

    override fun dispose() {

    }

    override fun resize(width: Int, height: Int) {

    }

    private fun createPlayer() {
        val bDef = BodyDef()
        var shape = PolygonShape()
        val fDef = FixtureDef()

        bDef.position.set(100f / PPM, 200f / PPM)
        bDef.type = DynamicBody
        bDef.linearVelocity.set(1f, 0f) //Make the player always go right
        val body: Body = world.createBody(bDef)

        shape.setAsBox(13f / PPM, 13f / PPM)
        fDef.shape = shape
        fDef.filter.categoryBits = BIT_PLAYER
        fDef.filter.maskBits = BIT_RED or BIT_CRYSTAL
        body.createFixture(fDef).userData = "player"

        //Foot sensor
        shape.setAsBox(13f / PPM, 2f / PPM, Vector2(0f, -13f / PPM), 0f)
        fDef.shape = shape
        fDef.filter.categoryBits = BIT_PLAYER
        fDef.filter.maskBits = BIT_RED
        fDef.isSensor = true
        body.createFixture(fDef).userData = "foot"

        //Create player
        player = Player(body)

        body.userData = player
    }

    private fun createTiles() {
        tileSize = tileMap.properties.get("tilewidth", Float::class.java)
        var layer: TiledMapTileLayer = tileMap.layers["red"] as TiledMapTileLayer
        createLayer(layer, BIT_RED)

        layer = tileMap.layers["green"] as TiledMapTileLayer
        createLayer(layer, BIT_GREEN)

        layer = tileMap.layers["blue"] as TiledMapTileLayer
        createLayer(layer, BIT_BLUE)
    }

    private fun createLayer(layer: TiledMapTileLayer, bits: Short) {
        val bDef = BodyDef()

        //Go through all the cells in the layer
        for (row in 0 until layer.height) {
            for (col in 0 until layer.width) {
                //Get cell
                val cell: Cell = layer.getCell(col, row) ?: continue

                if (cell.tile == null) continue

                //Create body + fixture
                bDef.type = StaticBody
                bDef.position.set((col + 0.5f) * tileSize / PPM, (row + 0.5f) * tileSize / PPM)

                val vector2 = Array<Vector2>(3, { i -> Vector2.Zero })

                vector2[0] = Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM) //Bottom left corner
                vector2[1] = Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM) //Top left corner
                vector2[2] = Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM) //Top Right corner

                world.createBody(bDef).chain(vector2[0], vector2[1], vector2[2]) {
                    friction = 0f
                    filter.categoryBits = bits
                    filter.maskBits = BIT_PLAYER
                    isSensor = false
                }
            }
        }
    }

    private fun createCrystals() {
        crystals = mutableListOf()

        var layer = tileMap.layers.get("crystals")

        val bDef = BodyDef()
        val fDef = FixtureDef()

        for (mo in layer.objects) {
            bDef.type = StaticBody
            val x = mo.properties.get("x", Float::class.java) / PPM
            val y = mo.properties.get("y", Float::class.java) / PPM
            bDef.position.set(x, y)

            val cshape = CircleShape()
            cshape.radius = 8f / PPM

            fDef.shape = cshape
            fDef.isSensor = true
            fDef.filter.categoryBits = BIT_CRYSTAL
            fDef.filter.maskBits = BIT_PLAYER

            val body = world.createBody(bDef)
            body.createFixture(fDef).userData = "crystal"

            val type = mo.properties.get("type")

            val c = Crystal(body)
            body.userData = c
            crystals.add(c)
        }
    }

    private fun switchBlocks() {
        val body = player.body.fixtureList.first()
        val foot = player.body.fixtureList[1]

        // get current bits set on player's body
        var bits = body.filterData.maskBits
        // temp filter data to hold data and set back to each fixture
        var tmpFilterData: Filter

        // switch to next color
        // red -> green -> blue -> red
        if ((bits and BIT_RED) != 0.toShort()) {
            bits = bits and BIT_RED.inv() //Remove the red
            bits = bits or BIT_GREEN //Add the green
        } else if ((bits and BIT_GREEN) != 0.toShort()) {
            bits = bits and BIT_GREEN.inv() //Remove the green
            bits = bits or BIT_BLUE //Add the blue
        } else if ((bits and BIT_BLUE) != 0.toShort()) {
            bits = bits and BIT_BLUE.inv() //Remove the blue
            bits = bits or BIT_RED //Add the red
        }

        // set new mask bits to body
        tmpFilterData = body.filterData
        tmpFilterData.maskBits = bits
        body.filterData = tmpFilterData

        // set new mask bits to foot
        tmpFilterData = foot.filterData
        tmpFilterData.maskBits = bits and BIT_CRYSTAL.inv()
        foot.filterData = tmpFilterData
    }
}