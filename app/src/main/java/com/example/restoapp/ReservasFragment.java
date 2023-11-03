package com.example.restoapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.restoapp.controladores.ReservationBD;
import com.example.restoapp.modelos.Reservation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservasFragment extends Fragment implements DatePickerFragment.DateSelectionListener, TimePickerFragment.TimeSelectionListener{
    private ListView listView;
    private ArrayList<Integer> idReserve;
    private ReservationBD reservationBD;
    private Context context;
    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;
    private int selectedHour;
    private int selectedMinute;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        reservationBD = new ReservationBD(context);
        Log.d("ReservasFragment", "reservationBD se inicializó correctamente");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireContext();
        reservationBD = new ReservationBD(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservas, container, false);
        listView = view.findViewById(R.id.reservation_list);

        fillListView(view);

        Button botonAgregarReserva = view.findViewById(R.id.botonAgregarReserva);
        botonAgregarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogAgregarReserva();
            }
        });

        return view;
    }

    private void fillListView(View view) {
        idReserve = new ArrayList<Integer>();

        List<Reservation> reservationList = reservationBD.lista();

        ReservationAdapter adapter = new ReservationAdapter(requireActivity(), R.layout.reservation_item, reservationList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Reservation reservation = reservationList.get(i);
                Bundle bolsa = new Bundle();
                bolsa.putInt("id", reservation.getId());
                bolsa.putInt("number_of_people", reservation.getNumber_of_people());
                bolsa.putString("dateAndTime", reservation.getDateAndTime());
                bolsa.putLong("created", reservation.getCreated().getTime());
                bolsa.putString("type",reservation.getType());
                bolsa.putInt("table", reservation.getTable());
                bolsa.putString("observations", reservation.getObservations());
                bolsa.putString("status", reservation.getStatus());
            }
        });
    }

    private void mostrarDialogAgregarReserva() {
        Dialog dialog = new Dialog(requireContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_reservation);

        Button selectDateTimeButton = dialog.findViewById(R.id.dateTimeButton);

        selectDateTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog();
            }
        });


        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );

        Button typeOfReservationButton = dialog.findViewById(R.id.typeOfReservation);
        Spinner typeOfReservationSpinner = dialog.findViewById(R.id.typeOfReservationSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.type_of_reservations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfReservationSpinner.setAdapter(adapter);

        typeOfReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeOfReservationSpinner.getVisibility() == View.VISIBLE) {
                    typeOfReservationSpinner.setVisibility(View.GONE);
                } else {
                    typeOfReservationSpinner.setVisibility(View.VISIBLE);
                }
            }
        });

        Button cantPersonasButton = dialog.findViewById(R.id.number_of_people);
        EditText numberOfPeopleEditText = dialog.findViewById(R.id.numberOfPeopleEditText);

        cantPersonasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOfPeopleEditText.getVisibility() == View.VISIBLE) {
                    numberOfPeopleEditText.setVisibility(View.GONE);
                } else {
                    numberOfPeopleEditText.setVisibility(View.VISIBLE);
                }
            }
        });

        Button selectTableButton = dialog.findViewById(R.id.select_table);
        EditText selectTableEditText = dialog.findViewById(R.id.selectTableEditText);

        selectTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectTableEditText.getVisibility() == View.VISIBLE) {
                    selectTableEditText.setVisibility(View.GONE);
                } else {
                    selectTableEditText.setVisibility(View.VISIBLE);
                }
            }
        });


        selectDateTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog();
            }
        });

        Button botonConfirmarReserva = dialog.findViewById(R.id.botonConfirmarReserva);
        botonConfirmarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner typeOfReservationSpinner = dialog.findViewById(R.id.typeOfReservationSpinner);
                EditText numberOfPeopleEditText = dialog.findViewById(R.id.numberOfPeopleEditText);
                EditText selectTableEditText = dialog.findViewById(R.id.selectTableEditText);

                String typeOfReservation = typeOfReservationSpinner.getSelectedItem().toString();
                int numberOfPeople = Integer.parseInt(numberOfPeopleEditText.getText().toString());
                int selectedTable = Integer.parseInt(selectTableEditText.getText().toString());

                Log.d("ReservasFragment", "typeOfReservation: " + typeOfReservation);
                Log.d("ReservasFragment", "numberOfPeople: " + numberOfPeople);
                Log.d("ReservasFragment", "selectedTable: " + selectedTable);

                // Aquí obtienes la fecha y hora seleccionada
                int year = selectedYear; // Obtén el año seleccionado
                int month = selectedMonth; // Obtén el mes seleccionado
                int day = selectedDay; // Obtén el día seleccionado
                int hour = selectedHour; // Obtén la hora seleccionada
                int minute = selectedMinute; // Obtén el minuto seleccionado

                // Ahora creamos un objeto Reservation con los valores obtenidos
                String dateAndTime = String.format("%04d-%02d-%02d %02d:%02d", year, month, day, hour, minute);
                Log.d("ReservasFragment", "Fecha seleccionada: " + selectedYear + "-" + selectedMonth + "-" + selectedDay);
                Log.d("ReservasFragment", "Hora seleccionada: " + selectedHour + ":" + selectedMinute);

                Date createdDate = new Date(); // Aquí deberías obtener la fecha actual o la que corresponda
                Reservation newReservation = new Reservation(0, numberOfPeople, dateAndTime,createdDate,
                        typeOfReservation,  selectedTable,"observacion", "Pendiente");

                // Agregamos la reserva a la base de datos
                Log.d("ReservasFragment", "Valores de newReservation: " + newReservation.toString());
                Log.d("ReservasFragment", "newReservation es null: " + (newReservation == null));

                reservationBD.agregar(newReservation);

                dialog.dismiss();
            }
        });

        TextView btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }




    private void showDateTimePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getParentFragmentManager(), "datePicker");
        datePickerFragment.dateListener = new DatePickerFragment.DateSelectionListener() {
            @Override
            public void onDateSelected(int year, int month, int day) {
                // Aquí puedes manejar la fecha seleccionada
                // Después de seleccionar la fecha, abre el fragmento de selección de hora.

                selectedYear = year; // Asigna el año seleccionado
                selectedMonth = month; // Asigna el mes seleccionado
                selectedDay = day; // Asigna el día seleccionado
                showTimePickerDialog();
            }
        };


    }

    private void showTimePickerDialog() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getParentFragmentManager(), "timePicker");
        timePickerFragment.timeListener = new TimePickerFragment.TimeSelectionListener() {
            @Override
            public void onTimeSelected(int hour, int minute) {
                // Aquí puedes manejar la hora seleccionada
                selectedHour = hour; // Asigna la hora seleccionada
                selectedMinute = minute; // Asigna el minuto seleccionado
            }
        };
    }

    @Override
    public void onDateSelected(int year, int month, int dayOfMonth) {
        selectedYear = year;
        selectedMonth = month;
        selectedDay = dayOfMonth;
    }

    @Override
    public void onTimeSelected(int hour, int minute) {
        selectedHour = hour;
        selectedMinute = minute;

    }
}
