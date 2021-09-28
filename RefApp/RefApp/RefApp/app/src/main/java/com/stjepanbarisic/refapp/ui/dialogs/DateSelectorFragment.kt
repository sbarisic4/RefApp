package com.stjepanbarisic.refapp.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatDialogFragment
import java.util.*


const val DATE_SELECTOR_ID = "ID"
const val DATE_SELECTOR_TITLE = "TITLE"
const val DATE_SELECTOR_DATE = "DATE"

class DateSelectorFragment : AppCompatDialogFragment(), DatePickerDialog.OnDateSetListener {

    private var dialogId = 0
    private var listener: DatePickerDialog.OnDateSetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val cal = GregorianCalendar()
        var title: String? = null

        val arguments = arguments
        if (arguments != null) {
            dialogId = arguments.getInt(DATE_SELECTOR_ID)
            title = arguments.getString(DATE_SELECTOR_TITLE)

            val givenDate = arguments.getSerializable(DATE_SELECTOR_DATE) as Date?
            if (givenDate != null) {
                cal.time = givenDate
            }
        }

        val year = cal.get(GregorianCalendar.YEAR)
        val month = cal.get(GregorianCalendar.MONTH)
        val day = cal.get(GregorianCalendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), this, year, month, day)
        if (title != null) {
            dpd.setTitle(title)
        }

        return dpd
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = try {
            if (parentFragment == null) {
                throw TypeCastException()
            } else {
                parentFragment as DatePickerDialog.OnDateSetListener
            }

        } catch (e: TypeCastException) {
            try {
                context as DatePickerDialog.OnDateSetListener
            } catch (e: ClassCastException) {
                throw ClassCastException("Activity $context must implement OnDateSetListener interface")
            }
        } catch (e: ClassCastException) {
            throw ClassCastException("Fragment $parentFragment must implement OnDateSetListener interface")
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        view.tag = dialogId

        listener?.onDateSet(view, year, month, dayOfMonth)
    }
}