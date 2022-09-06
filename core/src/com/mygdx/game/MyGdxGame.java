package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Anim animation;
	int clickes;
	boolean dir;
	private boolean goRight = true;
	private int animPositionX = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		animation = new Anim("1sprite.png", 9,6, Animation.PlayMode.LOOP);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		animation.setTime(Gdx.graphics.getDeltaTime());
		float x = Gdx.input.getX() - animation.getFrame().getRegionWidth()/2.0f;
		float y = Gdx.graphics.getHeight() - Gdx.input.getY() - animation.getFrame().getRegionHeight()/2.0f;
		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) clickes++;
		Gdx.graphics.setTitle("Clicked" + clickes + "times!");

		if (Gdx.input.isKeyJustPressed(Input.Keys.L)) dir = true;
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) dir = false;
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) goRight = false;
		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) goRight = true;
		if (animPositionX + 320  >= Gdx.graphics.getWidth()) goRight = false;
		if (animPositionX <= 0) goRight = true;
		if (!animation.getFrame().isFlipX() && !goRight) animation.getFrame().flip(true, false);
		if (animation.getFrame().isFlipX() && goRight) animation.getFrame().flip(true, false);
		if (goRight) {
			animPositionX += 5;
		} else {
			animPositionX -= 5;
		}
		batch.begin();
		batch.draw(animation.getFrame(), animPositionX, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		animation.dispose();
	}
}
