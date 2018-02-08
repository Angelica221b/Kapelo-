package agtzm.kapelo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class agendarcitaFragment extends Fragment {
    String tipoCita[] = {"Corte de cabello", "Tinte", "Maquillaje", "Uñas acrilicas"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.agendarcita,container,false);
        Spinner spiner1 = (Spinner)view.findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,tipoCita);
        spiner1.setAdapter(adapter);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnAceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Cita agendada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onStart() {
        super.onStart();

        EditText txtDate = (EditText) getView().findViewById(R.id.txtdate);
        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    fecha dialog = new fecha(view);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft,"Date picker");
                }
            }

        });

    }
}