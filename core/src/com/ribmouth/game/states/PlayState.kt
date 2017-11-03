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
import com.ribmouth.game.handlers.B2DVars.Companion.BIT_BALL
import com.ribmouth.game.handlers.B2DVars.Companion.BIT_BOX
import com.ribmouth.game.handlers.B2DVars.Companion.BIT_GROUND
import com.ribmouth.game.handlers.B2DVars.Companion.PPM
import com.ribmouth.game.handlers.ContactListener
import com.ribmouth.game.handlers.GameStateManager
import kotlin.experimental.or

/**
 * Created by RibMouth on 2/11/2017.
 */
class PlayState(gsm: GameStateManager) : GameState(gsm) {
    private val world: World = World(Vector2(0f, -0.81f), true) //Normally -9.81f (earth's gravity)
    private val b2dr: Box2DDebugRenderer = Box2DDebugRenderer()
    private val b2dCam: OrthographicCamera = OrthographicCamera()

    init {
        world.setContactListener(ContactListener())

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
        fDef.filter.maskBits = BIT_BOX or BIT_BALL //Can collide with shapes having these categoryBits
        body.createFixture(fDef).userData = "ground"

        //Create falling box
        bDef.position.set(160f / PPM, 200f / PPM)
        bDef.type = DynamicBody
        body = world.createBody(bDef)
        shape.setAsBox(5f / PPM, 5f / PPM)
        fDef.shape = shape
        fDef.filter.categoryBits = BIT_BOX
        fDef.filter.maskBits = BIT_GROUND
        body.createFixture(fDef).userData = "box"

        //Create ball
        bDef.position.set(153f / PPM, 220f / PPM)
        body = world.createBody(bDef)
        var cShape: CircleShape = CircleShape()
        cShape.radius = 5 / PPM
        fDef.shape = cShape
        //fDef.restitution = 0.2f //Bounciness
        fDef.filter.categoryBits = BIT_BALL
        fDef.filter.maskBits = BIT_GROUND
        body.createFixture(fDef).userData = "ball"

        //Setup cam
        b2dCam.setToOrtho(false, WIDTH / PPM, HEIGHT / PPM)
    }

    override fun handleInput() {

    }

    override fun update(dt: Float) {
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