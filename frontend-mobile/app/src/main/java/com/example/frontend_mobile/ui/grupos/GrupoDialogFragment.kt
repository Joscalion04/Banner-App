package com.example.frontend_mobile.ui.grupos

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.frontend_mobile.R
import com.example.frontend_mobile.data.model.Alumno
import com.example.frontend_mobile.data.model.Grupo

class GrupoDialogFragment(private val alumno: Alumno) : DialogFragment() {

    private lateinit var grupoFragment: GrupoFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grupo_dialog, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<ImageButton>(R.id.btnClose)?.apply {
            setOnClickListener { dismiss() }
            visibility = View.VISIBLE
        }

        if (childFragmentManager.findFragmentById(R.id.containerGrupo) == null) {
            grupoFragment = GrupoFragment()

            arguments?.let { grupoFragment.arguments = it }

            grupoFragment.listener = object : GrupoAdapter.OnGrupoClickListener {
                override fun onGrupoClick(grupo: Grupo) {
                    dismiss()
                    (parentFragment as? OnGrupoSelectedListener)?.onGrupoSelected(grupo, alumno)
                }

                override fun onGrupoLongClick(grupo: Grupo): Boolean {
                    return true
                }

                override fun isDialogMode(): Boolean {
                    return true
                }
            }

            childFragmentManager
                .beginTransaction()
                .replace(R.id.containerGrupo, grupoFragment)
                .commitNow()
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    companion object {
        fun newInstance(alumno: Alumno) = GrupoDialogFragment(alumno)
    }

    interface OnGrupoSelectedListener {
        fun onGrupoSelected(grupo: Grupo, alumno: Alumno)
    }
}