package com.example.detectorcolor;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static int CODIGO_BUSCAR_IMAGEN = 1;
	private Button buscar_imageButton;
	private ImageView imageView;
	private TextView rojo_textView;
	private TextView verde_textView;
	private TextView azul_textView;
	private TextView frame_color;
	private TextView muestraParecidoColor;
	private TextView nombreParecidoColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buscar_imageButton = (Button) findViewById(R.id.buscar_button);
        imageView = (ImageView) findViewById(R.id.imageView);
        rojo_textView = (TextView) findViewById(R.id.rojo_textView);
        verde_textView = (TextView) findViewById(R.id.verde_textView);
        azul_textView = (TextView) findViewById(R.id.azul_textView);
        frame_color = (TextView) findViewById(R.id.muestra_textView);
        muestraParecidoColor = (TextView) findViewById(R.id.muestraParecidoColor_textView);
        nombreParecidoColor = (TextView) findViewById(R.id.nombreParecidoColor_textView);
        
        buscar_imageButton.setOnClickListener(buscarImagen);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    OnClickListener buscarImagen = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_PICK);
			startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"),
					CODIGO_BUSCAR_IMAGEN);
		}
	};
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CODIGO_BUSCAR_IMAGEN) {
			if (resultCode == RESULT_OK) {
				imageView.setImageURI(data.getData());
				procesarImagen();
			}
			
		}
	};
	
	private void procesarImagen() {
		Bitmap imagen = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
		long rojos = 0L;
		long verdes = 0L;
		long azules = 0L;
		int[] pixeles = new int[imagen.getWidth() * imagen.getHeight()];
		imagen.getPixels(pixeles, 0, imagen.getWidth(), 0, 0, imagen.getWidth(), imagen.getHeight());
		for (int cursor = 0; cursor < pixeles.length; cursor++) {
			rojos += Color.red(pixeles[cursor]);
			verdes += Color.green(pixeles[cursor]);
			azules += Color.blue(pixeles[cursor]);
		}
		long numPixels = imagen.getWidth() * imagen.getHeight();
		rojos /= numPixels;
		verdes /= numPixels;
		azules /= numPixels;
		
		rojo_textView.setText("Promedio de rojos: " + rojos);
		verde_textView.setText("Promedio de verdes: " + verdes);
		azul_textView.setText("Promedio de azules: " + azules);
		frame_color.setBackgroundColor(Color.rgb((int)rojos, (int)verdes, (int)azules));
		
		Punto3D[] pixelesRef = {
				new Punto3D(255, 0, 0),
				new Punto3D(0, 255, 0),
				new Punto3D(0, 0, 255),
				new Punto3D(255, 255, 0),
				new Punto3D(0, 255, 255),
				new Punto3D(255, 0, 255),
				new Punto3D(0, 0, 0),
				new Punto3D(255, 255, 255)
		};
		Punto3D pixelActual = new Punto3D(rojos, verdes, azules);
		
		double[] distancias = {
				pixelActual.distancia(pixelesRef[0]),
				pixelActual.distancia(pixelesRef[1]),
				pixelActual.distancia(pixelesRef[2]),
				pixelActual.distancia(pixelesRef[3]),
				pixelActual.distancia(pixelesRef[4]),
				pixelActual.distancia(pixelesRef[5]),
				pixelActual.distancia(pixelesRef[6]),
				pixelActual.distancia(pixelesRef[7])
		};
		String[] colores = {"Rojo", "Verde", "Azul", 
							"Amarillo", "Cyan", "Magenta",
							"Negro", "Blanco"};
		
		double dist_minima = 255;
		int indice_minima = 0;
		for (int index = 0; index < distancias.length; index++) {
			if (distancias[index] <= dist_minima) {
				indice_minima = index;
				dist_minima = distancias[index];
			}
			Log.i("Distancias", "Distancias en pos " + index + ": " + distancias[index]);
		}
		
		muestraParecidoColor.setBackgroundColor(Color.rgb((int)pixelesRef[indice_minima].getX(),
				(int)pixelesRef[indice_minima].getY(),
				(int)pixelesRef[indice_minima].getZ()));
		nombreParecidoColor.setText(colores[indice_minima]);
	}
    
}
