package com.medvedev.presentation.ui.fragments.drivers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.medvedev.mechanic.R
import com.medvedev.domain.pojo.Driver
import com.medvedev.mechanic.databinding.FragmentDriverDetailsBinding
import com.medvedev.presentation.ui.OnEditingFinishedListener
import com.medvedev.presentation.viewmodel.DriverViewModel
import kotlinx.coroutines.launch

class DriverDetailsFragment : Fragment() {

    private var _binding: FragmentDriverDetailsBinding? = null
    private val binding: FragmentDriverDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentDriverDetailsBinding == null")

    private val driverViewModel: DriverViewModel by lazy {
        ViewModelProvider(this)[DriverViewModel::class.java]
    }

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var idDriver: String? = null

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
        _binding = FragmentDriverDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        lifecycleScope.launch {
            var driver: Driver? = null

            idDriver?.let {
                driver = driverViewModel.getDriverById(it)
            }

            if (driver == null) {
                showToast(resources.getText(R.string.id_not_found))
                driverViewModel.finishWork()
            }

            driver?.let {
                initDriver(it)
                setListeners(it, idDriver)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseParams() {
        idDriver = requireArguments().getString(ID_DRIVER)
    }

    private fun observeViewModel() {
        driverViewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    private fun initDriver(driver: Driver) {
        with(binding) {
            tvName.text = driver.name
            tvSurname.text = driver.surname
            tvMiddleName.text = driver.middleName
            tvBirthday.text = driver.birthday
            tvDrivingLicenseNumber.text = driver.drivingLicenseNumber
            tvDrivingLicenseValidity.text = driver.drivingLicenseValidity
            tvMedicalCertificate.text = driver.medicalCertificateValidity
        }
    }

    private fun setListeners(driver: Driver, idDriver: String?) {
        binding.delete.setOnClickListener {
            lifecycleScope.launch {
                driverViewModel.deleteDriver(driver)
                driverViewModel.finishWork()
            }
        }

        binding.edit.setOnClickListener {
            launchFragment(DriverEditFragment.getInstanceDriverEdit(idDriver))
        }
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun launchFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.driver_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val ID_DRIVER = "ID_DRIVER"

        fun getInstance(idDriver: String): DriverDetailsFragment {
            return DriverDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_DRIVER, idDriver)
                }
            }
        }
    }
}