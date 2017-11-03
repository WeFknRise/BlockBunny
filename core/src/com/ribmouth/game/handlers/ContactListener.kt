package com.ribmouth.game.handlers

import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.ContactListener

/**
 * Created by RibMouth on 3/11/2017.
 */
class ContactListener : ContactListener {
    var playerOnGround: Boolean = false

    //Called when 2 fixtures start to collide
    override fun beginContact(contact: Contact?) {
        val fa: Fixture? = contact?.fixtureA
        val fb: Fixture? = contact?.fixtureB

        if(fa?.userData == null || fb?.userData == null) return

        if(fa.userData == "foot" || fb.userData == "foot") {
            playerOnGround = true
        }
    }

    //Called when 2 fixtures no longer collide
    override fun endContact(contact: Contact?) {
        val fa: Fixture? = contact?.fixtureA
        val fb: Fixture? = contact?.fixtureB

        if(fa?.userData == null || fb?.userData == null) return

        if(fa.userData == "foot" || fb.userData == "foot") {
            playerOnGround = false
        }
    }

    //Collision detection
    //PreSolve
    //Collision handling
    //PostSolve
    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {

    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {

    }
}