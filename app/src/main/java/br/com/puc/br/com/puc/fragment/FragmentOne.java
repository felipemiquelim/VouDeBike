package br.com.puc.br.com.puc.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.puc.facebookproject.R;
import br.com.puc.facebookproject.ciclista.gerenciar_ciclista;
import br.com.puc.facebookproject.shareactivity;

/**
 * Created by Felipe on 31/10/2015.
 */
public class FragmentOne extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentone,container,false);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent(getActivity(), gerenciar_ciclista.class);
        getActivity().startActivity(i);
    }
}
