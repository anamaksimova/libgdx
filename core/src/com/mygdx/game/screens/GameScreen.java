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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;
import com.mygdx.game.PhysX;

import java.util.ArrayList;

public class GameScreen implements Screen {
    private final Main game;
    private final SpriteBatch batch;
    private final Anim animation;
    private final Texture img;
//    private final Rectangle mapSize;
//    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer mapRenderer;
//    private TiledMap map;
    private Vector2 mapPosition;
    private float STEP = 3;
    private PhysX physX;
    private final int[] bg;
    private final int[] l1;
    private Body body;
    public static ArrayList<Body> bodies;
    private final Rectangle heroRect;

    public GameScreen(Main game) {
        bodies = new ArrayList<>();
        animation = new Anim("1sprite.png", 9,6, Animation.PlayMode.LOOP);
        this.game = game;
        batch = new SpriteBatch();
        img = new Texture("menu2.jpg");
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1.25f;

        TmxMapLoader tm = new TmxMapLoader();
//        map = tm.load("map/map1.tmx");
        TiledMap map = new TmxMapLoader().load("map/map2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        bg = new int[1];
        bg[0] = map.getLayers().getIndex("фон");
        l1 = new int[2];
        l1[0] = map.getLayers().getIndex("Слой 2");
        l1[1] = map.getLayers().getIndex("Слой 3");

        physX = new PhysX();
        map.getLayers().get("объекты").getObjects().getByType(RectangleMapObject.class); //выбор объектов по типу
//        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("объекты").getObjects().get("камера"); //выбор объекта по имени
//        camera.position.x = tmp.getRectangle().x;
//        camera.position.y = tmp.getRectangle().y;
//        tmp = (RectangleMapObject) map.getLayers().get("объекты").getObjects().get("камера");
//        mapSize = tmp.getRectangle();
        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("сеттинг").getObjects().get("hero"); //выбор объекта по имени
        heroRect = tmp.getRectangle();
        body = physX.addObject(tmp);

        Array<RectangleMapObject> objects = map.getLayers().get("объекты").getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects.size; i++) {
            physX.addObject(objects.get(i));
        }

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) body.applyForceToCenter (new Vector2(-10000, 0), true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) body.applyForceToCenter (new Vector2(10000, 0), true);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) body.applyForceToCenter (new Vector2(0, 1000000), true);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) camera.position.y -= STEP;
        if (Gdx.input.isKeyPressed(Input.Keys.P)) camera.zoom += 0.01f;
        if (Gdx.input.isKeyPressed(Input.Keys.O) && camera.zoom > 0) camera.zoom -= 0.01f;
        camera.position.x = body.getPosition().x;
        camera.position.y = body.getPosition().y;
        camera.update();

        ScreenUtils.clear(Color.PURPLE);
        mapRenderer.setView(camera);
        mapRenderer.render(bg);
        System.out.println(body.getLinearVelocity());
        batch.setProjectionMatrix(camera.combined);
        heroRect.x = body.getPosition().x - heroRect.width/2;
        heroRect.y = body.getPosition().y - heroRect.height/2;
        batch.begin();

        batch.draw(animation.getFrame(), heroRect.x, heroRect.y, heroRect.width, heroRect.height);
        batch.end();


        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }

        mapRenderer.render(l1);

        physX.step();
        physX.debugDraw(camera);

        for (int i = 0; i < bodies.size() ; i++) {
            physX.destroyBody(bodies.get(i));
        }
        bodies.clear();
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
        this.physX.dispose();
        this.animation.dispose();
        this.mapRenderer.dispose();
    }
}