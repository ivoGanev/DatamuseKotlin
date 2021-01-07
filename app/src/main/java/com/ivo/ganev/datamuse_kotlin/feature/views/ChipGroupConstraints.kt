/**
 * Copyright (C) 2020 Ivo Ganev Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ivo.ganev.datamuse_kotlin.feature.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.ivo.ganev.datamuse_kotlin.endpoint.words.HardConstraint

class ChipGroupConstraints(context: Context, attrs: AttributeSet) : ChipGroup(context, attrs),
    View.OnClickListener {
    private val maxConstraints = 3
    private val constraints = mutableListOf<Pair<Chip, HardConstraint>>()

    fun addConstraint(hardConstraint: HardConstraint): Boolean {
        if (constraints.size >= maxConstraints)
            return false
        val chip = Chip(context)

        constraints.add(Pair(chip, hardConstraint))
        chip.setOnCloseIconClickListener(this)

        chip.text = when (hardConstraint) {
            is HardConstraint.RelatedWords -> "Rel: ${hardConstraint.key}, ${hardConstraint.value}"
            is HardConstraint.MeansLike -> "Ml: ${hardConstraint.value}"
            is HardConstraint.SoundsLike -> "Sl: ${hardConstraint.value}"
            is HardConstraint.SpelledLike -> "Sp: ${hardConstraint.value}"
        }
        chip.setBackgroundColor(Color.BLUE)
        chip.isCloseIconVisible = true

        for (element in constraints)
            println(element)
        // order is not important when adding a constraint because datamuse api
        // doesn't utilize ordered constraints
        addView(chip, 0, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        println(this.childCount)
        return true
    }

    override fun onClick(v: View?) {
        if (v != null) {
            val index = constraints.indexOfFirst { it.first.id == v.id }
            val e = constraints[index]
            println("Removing: ${e.first.text}, ${e.second}")
            removeView(e.first)
            constraints.removeAt(index)
        }
    }

    fun getConstraints() = constraints.map { it.second }
}