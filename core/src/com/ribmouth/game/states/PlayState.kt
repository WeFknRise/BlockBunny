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
import com.ribmouth.game.handlers.B2DVars.Companion.BIT_BLUE
import com.ribmouth.game.handlers.B2DVars.Companion.BIT_GREEN
import com.ribmouth.game.handlers.B2DVars.Companion.BIT_PLAYER
import com.ribmouth.game.handlers.B2DVars.Companion.BIT_RED
import com.ribmouth.game.handlers.B2DVars.Companion.PPM
import com.ribmouth.game.handlers.BBInput
import com.ribmouth.game.handlers.BBInput.Companion.BUTTON1
import com.ribmouth.game.handlers.ContactListener
import com.ribmouth.game.handlers.GameStateManager
import ktx.box2d.chain

/**
 * Created by RibMouth on 2/11/2017.
 */
class PlayState(gsm: GameStateManager) : GameState(gsm) {
    private val world: World = World(Vector2(0f, -9.81f), true)
    private val b2dr: Box2DDebugRenderer = Box2DDebugRenderer()
    private val b2dCam: OrthographicCamera = OrthographicCamera()
    lateinit private var playerBody: Body
    private val contactListener: ContactListener = ContactListener()
    private var tileSize: Float = 0f

    //Map loader and renderer
    private val tileMap: TiledMap = TmxMapLoader().load("maps/level1.tmx")
    private val tileMapRenderer: OrthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(tileMap)

    init {
        //Setup box2d stuff
        world.setContactListener(contactListener)

        //Create player
        createPlayer()

        //Create tiles
        createTiles()

        //Setup cam
        b2dCam.setToOrtho(false, WIDTH / PPM, HEIGHT / PPM)
    }

    override fun handleInput() {
        if(BBInput.isPressed(BUTTON1)) {
            if(contactListener.playerOnGround) {
                playerBody.applyForceToCenter(0f, 200f, true) //Force is in Newtons upwards force
            }
        }
    }

    override fun update(dt: Float) {
        handleInput()

        world.step(dt, 6, 2)
    }

    override fun render() {
        //Clear screen
        Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        //Draw tileMap
        tileMapRenderer.setView(cam)
        tileMapRenderer.render()

        //Draw world
        b2dr.render(world, b2dCam.combined)
    }

    override fun dispose() {

    }

    override fun resize(width: Int, height: Int) {

    }

    private fun createPlayer() {
        val bDef = BodyDef()
        var shape = PolygonShape()
        val fDef = FixtureDef()

        bDef.position.set(160f / PPM, 200f / PPM)
        bDef.type = DynamicBody
        playerBody = world.createBody(bDef)

        shape.setAsBox(5f / PPM, 5f / PPM)
        fDef.shape = shape
        fDef.filter.categoryBits = BIT_PLAYER
        fDef.filter.maskBits = BIT_RED
        playerBody.createFixture(fDef).userData = "player"

        //Foot sensor
        shape.setAsBox(2f / PPM, 2f / PPM, Vector2(0f, -5f / PPM), 0f)
        fDef.shape = shape
        fDef.filter.categoryBits = BIT_PLAYER
        fDef.filter.maskBits = BIT_RED
        fDef.isSensor = true
        playerBody.createFixture(fDef).userData = "foot"
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

                if(cell.tile == null) continue

                //Create body + fixture
                bDef.type = StaticBody
                bDef.position.set((col + 0.5f) * tileSize / PPM, (row + 0.5f) * tileSize / PPM)

                //val chainShape = ChainShape()
                val vector2 = Array<Vector2>(3, { i -> Vector2.Zero })

                vector2[0] = Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM) //Bottom left corner
                vector2[1] = Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM) //Top left corner
                vector2[1] = Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM) //Top Right corner

                world.createBody(bDef).chain(vector2[0], vector2[1], vector2[2]) {
                    friction = 0f
                    filter.categoryBits = bits
                    filter.maskBits = BIT_PLAYER
                    isSensor = false
                }
            }
        }
    }
}