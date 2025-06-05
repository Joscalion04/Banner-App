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
            // Creamos la instancia de GrupoFragment
            grupoFragment = GrupoFragment()

            // Transferir argumentos si los hubiera
            arguments?.let { grupoFragment.arguments = it }

            // **Importante**: antes de hacer el commit, asignamos su listener
            // para que, cuando el usuario haga clic en un grupo, el callback
            // llame a `parentFragment as OnGrupoSelectedListener`.
            grupoFragment.listener = object : GrupoAdapter.OnGrupoClickListener {
                override fun onGrupoClick(grupo: Grupo) {
                    // 1) Cerramos este diálogo
                    dismiss()
                    // 2) Notificamos al fragmento padre (AlumnoFragment) que implementa OnGrupoSelectedListener
                    (parentFragment as? OnGrupoSelectedListener)?.onGrupoSelected(grupo, alumno)
                }

                override fun onGrupoLongClick(grupo: Grupo): Boolean {
                    // Si estamos en modo diálogo, no queremos el long click
                    return true
                }

                override fun isDialogMode(): Boolean {
                    return true
                }
            }

            // Reemplazamos el FrameLayout @id/containerGrupo con nuestro GrupoFragment
            childFragmentManager
                .beginTransaction()
                .replace(R.id.containerGrupo, grupoFragment)
                .commitNow() // commitNow() para que el fragmento quede inmediatamente en estado “attached”
        }
    }

    override fun onResume() {
        super.onResume()
        // Ajustar tamaño del diálogo
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    companion object {
        fun newInstance(alumno: Alumno) = GrupoDialogFragment(alumno)
    }

    // Interfaz para comunicar la selección de grupo
    interface OnGrupoSelectedListener {
        fun onGrupoSelected(grupo: Grupo, alumno: Alumno)
    }
}