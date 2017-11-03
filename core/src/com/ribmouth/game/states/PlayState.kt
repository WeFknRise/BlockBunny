package com.ribmouth.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody
import com.ribmouth.game.Game.Companion.HEIGHT
import com.ribmouth.game.Game.Companion.WIDTH
import com.ribmouth.game.handlers.B2DVars.Companion.BIT_PLAYER
import com.ribmouth.game.handlers.B2DVars.Companion.BIT_GROUND
import com.ribmouth.game.handlers.B2DVars.Companion.PPM
import com.ribmouth.game.handlers.BBInput
import com.ribmouth.game.handlers.BBInput.Companion.BUTTON1
import com.ribmouth.game.handlers.BBInput.Companion.BUTTON2
import com.ribmouth.game.handlers.ContactListener
import com.ribmouth.game.handlers.GameStateManager

/**
 * Created by RibMouth on 2/11/2017.
 */
class PlayState(gsm: GameStateManager) : GameState(gsm) {
    private val world: World = World(Vector2(0f, -9.81f), true)
    private val b2dr: Box2DDebugRenderer = Box2DDebugRenderer()
    private val b2dCam: OrthographicCamera = OrthographicCamera()
    private val playerBody: Body
    private val contactListener: ContactListener = ContactListener()

    init {
        world.setContactListener(contactListener)

        //Create platform
        val bDef: BodyDef = BodyDef()
        bDef.position.set(160f / PPM, 120f / PPM)

        //static body - don't move, unaffected by forces
        //kinematic forces - don't get affected by forces but can move
        //dynamic body - always affected by forces
        bDef.type = StaticBody
        var body: Body = world.createBody(bDef)

        //Create fixtures
        var shape: PolygonShape = PolygonShape()
        shape.setAsBox(50f / PPM, 5f / PPM)
        val fDef: FixtureDef = FixtureDef()
        fDef.shape = shape
        fDef.filter.categoryBits = BIT_GROUND //Has category bit
        fDef.filter.maskBits = BIT_PLAYER //Can collide with shapes having these categoryBits
        body.createFixture(fDef).userData = "ground"

        //Create player
        bDef.position.set(160f / PPM, 200f / PPM)
        bDef.type = DynamicBody
        playerBody = world.createBody(bDef)

        shape.setAsBox(5f / PPM, 5f / PPM)
        fDef.shape = shape
        fDef.filter.categoryBits = BIT_PLAYER
        fDef.filter.maskBits = BIT_GROUND
        playerBody.createFixture(fDef).userData = "player"

        //Create foot sensor
        shape.setAsBox(2f / PPM, 2f / PPM, Vector2(0f, -5f / PPM), 0f)
        fDef.shape = shape
        fDef.filter.categoryBits = BIT_PLAYER
        fDef.filter.maskBits = BIT_GROUND
        fDef.isSensor = true
        playerBody.createFixture(fDef).userData = "foot"

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

        b2dr.render(world, b2dCam.combined)
    }

    override fun dispose() {

    }

    override fun resize(width: Int, height: Int) {

    }
}