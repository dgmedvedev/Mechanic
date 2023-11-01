package com.medvedev.presentation.ui.fragments.drivers

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.medvedev.domain.pojo.Driver
import com.medvedev.mechanic.BuildConfig
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.FragmentDriverEditBinding
import com.medvedev.presentation.ui.OnEditingFinishedListener
import com.medvedev.presentation.viewmodel.DriverViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DriverEditFragment : Fragment() {

    private var _binding: FragmentDriverEditBinding? = null
    private val binding: FragmentDriverEditBinding
        get() = _binding ?: throw RuntimeException(
            String.format(
                getString(R.string.binding_exception),
                binding.javaClass.simpleName
            )
        )

    private val driverViewModel: DriverViewModel by lazy {
        ViewModelProvider(this)[DriverViewModel::class.java]
    }

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var screenMode: String = MODE_UNKNOWN
    private var idDriver: String? = null

    private val pattern = Patterns.WEB_URL

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw java.lang.RuntimeException(getString(R.string.implement_exception))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriverEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        lifecycleScope.launch {
            var driver: Driver? = null
            idDriver?.let { id ->
                driver = driverViewModel.getDriverById(id)
                driver?.let {
                    initDriver(it)
                }
            }
            setListeners(driver)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseParams() {
        val args = requireArguments()

        if (!args.containsKey(EXTRA_SCREEN_MODE)) {
            throw java.lang.RuntimeException(getString(R.string.param_screen_mode_exception))
        }
        val mode = args.getString(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw java.lang.RuntimeException(getString(R.string.screen_mode_exception) + mode)
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(EXTRA_ID_DRIVER)) {
                throw java.lang.RuntimeException(getString(R.string.param_id_driver_exception))
            }
            idDriver = args.getString(EXTRA_ID_DRIVER)
        }
    }

    private fun observeViewModel() {
        driverViewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    private fun initDriver(driver: Driver) {
        with(binding) {
            etName.setText(driver.name)
            etSurname.setText(driver.surname)
            etMiddleName.setText(driver.middleName)
            etBirthday.setText(driver.birthday)
            etDrivingLicenseNumber.setText(driver.drivingLicenseNumber)
            etDrivingLicenseValidity.setText(driver.drivingLicenseValidity)
            etMedicalCertificate.setText(driver.medicalCertificateValidity)
        }
    }

    private suspend fun addDriver(driver: Driver?) {
        var id = System.currentTimeMillis().toString()
        val name = binding.etName.text.toString()
        val surname = binding.etSurname.text.toString()
        val middleName = binding.etMiddleName.text.toString()

        val birthday = binding.etBirthday.text.toString()
        val drivingLicenseNumber = binding.etDrivingLicenseNumber.text.toString()
        val drivingLicenseValidity = binding.etDrivingLicenseValidity.text.toString()
        val medicalCertificateValidity = binding.etMedicalCertificate.text.toString()

        // imageUrl в разработке
        //var imageUrl = user?.imageUrl ?: ""
        var imageUrl = getString(R.string.image_url)
        if (BuildConfig.DEBUG) {
            imageUrl = getString(R.string.image_url)
        }

        try {
            if (!pattern.matcher(imageUrl).matches()) throw HttpFormatException()

            if (idDriver != null) {
                driver?.let {
                    id = it.id
                    driverViewModel.deleteDriver(it)
                }
            }

            driverViewModel.insertDriver(
                Driver(
                    id,
                    if (name == "") resources.getString(R.string.name) else name,
                    if (surname == "") resources.getString(R.string.surname) else surname,
                    middleName,
                    imageUrl,
                    birthday,
                    drivingLicenseNumber,
                    drivingLicenseValidity,
                    medicalCertificateValidity
                )
            )
            driverViewModel.finishWork()
        } catch (hfe: HttpFormatException) {
            showToast(resources.getText(R.string.not_valid_url))
        }
    }

    private fun setListeners(driver: Driver?) {
        binding.save.setOnClickListener {
            lifecycleScope.launch {
                addDriver(driver)
            }
        }
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_screen_mode"
        private const val EXTRA_ID_DRIVER = "extra_id_driver"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun getInstanceDriverAdd(): DriverEditFragment {
            return DriverEditFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun getInstanceDriverEdit(idDriver: String?): DriverEditFragment {
            return DriverEditFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, MODE_EDIT)
                    putString(EXTRA_ID_DRIVER, idDriver)
                }
            }
        }
    }

    internal inner class HttpFormatException : Exception()
}