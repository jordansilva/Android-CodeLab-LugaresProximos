package com.codelab.android.lugaresproximos;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipula o mapa assim que ele estiver disponível.
     * Essa chamada de retorno (callback) é acionado quando o mapa estiver pronto para ser usado.
     * Esse é o lugar onde podemos adicionar marcadores ou linhas, adicionar listeners ou mover a
     * câmera. No nosso caso, iremos adicionar:
     * - Marcador para o Google Belo Horizonte;
     * - Setar o título do marcador para GoogleBH;
     * - Setar uma pequena informação sobre o lugar no marcador;
     * - Alterar o ícone/cor desse marcador;
     * - Por fim, iremos mover a câmera e o zoom para o marcador;
     *
     * Se o Google Play services não estiver instalado no dispositivo, será solicitado ao usuário
     * a instalação deste. Esse método e os procedimentos descritos acima só irão ocorrer uma vez
     * que o usuário tenha instalado Google Play services e retorne para o aplicativo.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Setando uma Latitude e Longitude para o marcador
        LatLng googleBh = new LatLng(-19.929532, -43.940726);

        //Criando um marcador
        MarkerOptions marker = new MarkerOptions();
        marker.position(googleBh); //Seta a posição do marcador
        marker.title("Google BH"); //Seta o título do marcador
        marker.snippet("International Women’s Day"); //Seta uma breve descrição sobre o marcador

        //Cria o marcador padrão com um tom de cor rosa.
        BitmapDescriptor cor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);
        marker.icon(cor);

        //Adicionando um marcador
        mMap.addMarker(marker);

        //Movimenta a camera Zoom no mapa
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(googleBh, 17.0f);
        mMap.moveCamera(cameraUpdate);
    }
}
