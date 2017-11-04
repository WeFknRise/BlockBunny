package com.ribmouth.game.handlers

import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.ContactListener

/**
 * Created by RibMouth on 3/11/2017.
 */
class ContactListener : ContactListener {
    private var numFootContacts: Int = 0
    var bodiesToRemove: MutableList<Body> = mutableListOf()
        private set

    var playerOnGround: Boolean = false
        get() = numFootContacts > 0

    //Called when 2 fixtures start to collide
    override fun beginContact(contact: Contact?) {
        val fa: Fixture? = contact?.fixtureA
        val fb: Fixture? = contact?.fixtureB

        if(fa == null || fb == null) return

        if(checkUserData("foot", fa) || checkUserData("foot", fb)) {
            numFootContacts++
        }

        if(checkUserData("crystal", fa)) {
            //Remove crystal
            bodiesToRemove.add(fa.body)
        }

        if (checkUserData("crystal", fb)) {
            //Remove crystal
            bodiesToRemove.add(fb.body)
        }
    }

    //Called when 2 fixtures no longer collide
    override fun endContact(contact: Contact?) {
        val fa: Fixture? = contact?.fixtureA
        val fb: Fixture? = contact?.fixtureB

        if(fa == null || fb == null) return

        if(checkUserData("foot", fa) || checkUserData("foot", fb)) {
            numFootContacts--
        }

        if(checkUserData("crystal", fa) || checkUserData("crystal", fb)) {

        }
    }

    private fun checkUserData(ud: String, f: Fixture): Boolean {
        return f.userData != null && f.userData == ud
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