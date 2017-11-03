package com.ribmouth.game.handlers

import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.ContactListener

/**
 * Created by RibMouth on 3/11/2017.
 */
class ContactListener : ContactListener {
    //Called when 2 fixtures start to collide
    override fun beginContact(contact: Contact?) {
        val fa: Fixture? = contact?.fixtureA
        val fb: Fixture? = contact?.fixtureB

        println("${fa?.userData}, ${fb?.userData}")
    }

    //Called when 2 fixtures no longer collide
    override fun endContact(contact: Contact?) {

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