package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;

public class GameScreen implements Screen {
    private final Main game;
    private final SpriteBatch batch;
    private final Anim animation;
    private final Texture img;
    private final Rectangle mapSize;
    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap map;
    private Vector2 mapPosition;
    private float STEP = 3;

    public GameScreen(Main game) {
        animation = new Anim("1sprite.png", 9,6, Animation.PlayMode.LOOP);
        this.game = game;
        batch = new SpriteBatch();
        img = new Texture("menu2.jpg");
//        startRect = new Rectangle(0, 0, img.getWidth(), img.getHeight());
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.25f;

        TmxMapLoader tm = new TmxMapLoader();
        map = tm.load("map/map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapPosition = new Vector2();
        map.getLayers().get("объекты").getObjects().getByType(RectangleMapObject.class); //выбор объектов по типу
        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("объекты").getObjects().get("камера"); //выбор объекта по имени
        camera.position.x = tmp.getRectangle().x;
        camera.position.y = tmp.getRectangle().y;
        tmp = (RectangleMapObject) map.getLayers().get("объекты").getObjects().get("камера");
        mapSize = tmp.getRectangle();




    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && mapSize.x < camera.position.x-1) camera.position.x -= STEP;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && mapSize.x + mapSize.width > camera.position.x+1) camera.position.x += STEP;
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && mapSize.y < camera.position.y-1) camera.position.y += STEP;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && mapSize.y - mapSize.height > camera.position.y+1) camera.position.y -= STEP;

        if (Gdx.input.isKeyPressed(Input.Keys.P)) camera.zoom += 0.01f;
        if (Gdx.input.isKeyPressed(Input.Keys.O) && camera.zoom > 0) camera.zoom -= 0.01f;
        camera.update();

        ScreenUtils.clear(Color.BROWN);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
        mapRenderer.setView(camera);
        mapRenderer.render();

        if (false) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.rect(mapSize.x, mapSize.y, mapSize.width, mapSize.height);
        shapeRenderer.end();
        batch.begin();
        animation.setTime(Gdx.graphics.getDeltaTime());
        TextureRegion trTmp = animation.getFrame();
        batch.draw(trTmp, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.img.dispose();
        this.shapeRenderer.dispose();
    }
}