package com.example.exemplograficos3;
/* M. F. P. Ledón, 20/05/2011 - 9 meses */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class AviaoView extends View {
	private Drawable imgAviao;
	private Drawable bandeira;
	private float x, y, xband, yband;
	private int passo = 20;
	private int larg, alt, largband, altband;
	private boolean pressionado = false;
	private Context ctx;



	public AviaoView(Context context, AttributeSet atts) {
		super(context, atts);
		ctx = context;
		// Colocamos a imagem de fundo da view
		setBackgroundResource(R.drawable.mundo);
		// Pegamos uma referência à imagem do avião:
		imgAviao = context.getResources().getDrawable(R.drawable.aviao);
		bandeira = context.getResources().getDrawable(R.drawable.italia);
		// Recuperamos a largura e altura das imagens:
		larg = imgAviao.getIntrinsicWidth();
		alt = imgAviao.getIntrinsicHeight();
		largband = bandeira.getIntrinsicWidth();
		altband = bandeira.getIntrinsicHeight();
		x = 15;  y = 120;  xband = -100;  yband = -100;
		// Permitimos que a view receba o foco e trate eventos de teclado:
		setFocusable(true);
	}

	@SuppressLint("UseCompatLoadingForDrawables")
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint p = new Paint();
		canvas.drawCircle(xband, yband, 8, p);
		p.setColor(0xFF00FF00); //verde
		canvas.drawCircle(xband, yband, 10, p); //círculo verde
		if(Math.abs(xband/this.getWidth()-0.366)<=0.09 && Math.abs(yband/this.getHeight()-0.861)<=0.09) {  //Brasil
			bandeira = getResources().getDrawable(R.drawable.brasil);
		}
		if(Math.abs(xband/this.getWidth()-0.742)<=0.03 && Math.abs(yband/this.getHeight()-0.414)<=0.03) {  //Itália
			bandeira = getResources().getDrawable(R.drawable.italia);
		}
		if(Math.abs(xband/this.getWidth()-0.1598)<=0.03 && Math.abs(yband/this.getHeight()-0.506)<=0.03) {  //Cuba
			bandeira = getResources().getDrawable(R.drawable.cuba);
		}
		if(Math.abs(xband/this.getWidth()-0.667)<=0.03 && Math.abs(yband/this.getHeight()-0.434)<=0.03) {  //Espanha
			bandeira = getResources().getDrawable(R.drawable.espana);
		}
		if(Math.abs(xband/this.getWidth()-0.188)<=0.09 && Math.abs(yband/this.getHeight()-0.406)<=0.09) {  //USA
			bandeira = getResources().getDrawable(R.drawable.usa);
		}
		//colocamos a bandeira na posição xband,yband:
		bandeira.setBounds((int)(xband + largband/2), (int)(yband-altband),(int)(xband + largband + largband/2), (int)yband);
		bandeira.draw(canvas);
		// Definimos a posição e o tamanho para desenhar o avião:
		imgAviao.setBounds((int)x, (int)(y - alt), (int)(x + larg), (int)y );
		imgAviao.draw(canvas);
	}

	@Override
	public boolean onKeyDown(int codigoTecla, KeyEvent evento) {
		//Para mover o avião por teclas de cursor
		boolean repintar = true;
		switch (codigoTecla) {
			case KeyEvent.KEYCODE_DPAD_UP:
				y -= passo; yband-=passo;
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				y += passo; yband += passo;
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				x -= passo; xband -= passo;
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				x += passo; xband += passo;
				break;
			default:
				repintar = false;
		}
		if (repintar) {
			invalidate();
			return true;
		}
		return super.onKeyDown(codigoTecla, evento);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Para mover o avião por operação de tipo drag and drop
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// Inicia o movimento se pressionou a imagem:
				pressionado = imgAviao.copyBounds().contains((int) x, (int) y);

				//Para identificar pontos no mapa, saber suas posições relativas,
				//independente das resoluções e tamanhos de telas dos diferentes aparelhos:
				//Toast.makeText(ctx, "x/this.getWidth(): " + x/this.getWidth()
				//		+ ", y/this.getHeight(): " + y/this.getHeight() + "\n\n\n", Toast.LENGTH_LONG).show();

				break;
			case MotionEvent.ACTION_MOVE:
				// Arrastamos o avião:
				if (pressionado) {
					this.x = (int) x - (larg / 2);
					this.y = (int) y - (alt / 4);
					this.xband = (int) x - largband/2;
					this.yband = (int) y - 2*altband;
				}
				break;
			case MotionEvent.ACTION_UP:
				// Terminamos o movimento, tocamos um som:
				if(pressionado) {
					Context ctx = getContext();
					AudioManager som = (AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);
					som.playSoundEffect(AudioManager.FX_KEY_CLICK);
				}
				pressionado = false;
				break;
		}
		invalidate();
		return true;
	}
}

