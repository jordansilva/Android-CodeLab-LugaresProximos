package com.codelab.android.lugaresproximos;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

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

        //Adicionando marcador do Google BH
        LatLng googleBh = new LatLng(-19.929532, -43.940726);
        float cor = BitmapDescriptorFactory.HUE_ROSE;
        adicionarMarcador("Google BH", "International Women's Day", googleBh, cor);

        //Movimenta a camera Zoom no mapa
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(googleBh, 17.0f);
        mMap.moveCamera(cameraUpdate);

        buscarRestaurantesProximos();
    }

    private void buscarRestaurantesProximos() {
        //Nearby URL
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-19.929532,-43.940726&radius=500&types=food&key=AIzaSyAhp_8i1i7H6l-IpqkKk8oU9uIjVt1nGcU
        String nearbyURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

        //Meus parâmetros
        String location = "location=-19.929532,-43.940726";
        String radius = "&radius=500";
        String types = "&types=food";
        String key = "&key=AIzaSyAhp_8i1i7H6l-IpqkKk8oU9uIjVt1nGcU";

        String url = nearbyURL + location + radius + types + key;

        requestUrl(url);
    }


    /**
     * Adicionar Marcador no Mapa
     */
    public void adicionarMarcador(String nome, String descricao, LatLng posicao, float cor) {

        //Criando marcador
        MarkerOptions marker = new MarkerOptions();
        marker.position(posicao); //Seta a posição do marcador
        marker.title(nome); //Seta o título do marcador
        marker.snippet(descricao); //Seta uma breve descrição sobre o marcador

        //Cria o marcador padrão com um tom de cor rosa.
        BitmapDescriptor icone = BitmapDescriptorFactory.defaultMarker(cor);
        marker.icon(icone);

        //Adicionando um marcador
        mMap.addMarker(marker);
    }

    //Requisitando a URL
    private void requestUrl(String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objeto = jsonArray.getJSONObject(i);

                        //Buscando nome e descrição
                        String nome = objeto.getString("name");
                        String snippet = objeto.getString("vicinity");

                        //Buscando posição
                        JSONObject location = objeto.getJSONObject("geometry").getJSONObject("location");
                        double latitude = location.getDouble("lat");
                        double longitude = location.getDouble("lng");
                        LatLng posicao = new LatLng(latitude, longitude);

                        //Adicionando marcador
                        adicionarMarcador(nome, snippet, posicao, BitmapDescriptorFactory.HUE_BLUE);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //Mostrar mensagem caso dê erro na requisição
                Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
            }
        });
    }
}
