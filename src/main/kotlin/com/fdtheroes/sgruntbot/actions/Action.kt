package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext

fun interface Action {
    fun doAction(ctx: ActionContext)
}