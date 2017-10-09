package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class MyBox2D extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	// переменная бокс 2д
	World world;
	// не буду использовать никакой графики
	Box2DDebugRenderer rend;

	OrthographicCamera camera;
	// обьект квадрат
	Body rect;
	
	@Override
	public void create () {
		// переменная бокс 2д (вектор гравитации(x, y), не считает стоячие тела)
		world = new World(new Vector2(0, - 10),true);

		// позиция в метрах
		camera = new OrthographicCamera(20, 15);
		// позиция камеры
		camera.position.set(new Vector2(10,7.5f), 0);
		rend = new Box2DDebugRenderer();

		createRect();
		createWall();
	}

	@Override
	public void render () {
		// очистка экрана
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// отрисовываем мир
		rend.render(world, camera.combined);
		// обновляем камеру
		camera.update();
		// сколько раз 1 сек. прощет кординат, сколько прощитывается ускорение,
		// и позиця ускорения, чем больше тем точнее - тем хуже производительность
		world.step(1/60f,4,4);


		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) rect.applyForceToCenter(new Vector2(- 50,0),true);
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) rect.applyForceToCenter(new Vector2(+ 50,0),true);
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) rect.applyForceToCenter(new Vector2(0,+ 400),true);
	}
	// создание квадрата
	private void createRect(){
		// показывает позицю тела и что оно собой являет(статическое, динамичское
		// , кинематическое - не взаемодествует с статическим или динамичским)
		BodyDef bDef = new BodyDef();
		// динамическое телео
		bDef.type = BodyDef.BodyType.DynamicBody;
		// позиция тела (в метрах)
		bDef.position.set(10, 7);
		// создается в мире 2д бокс
		rect = world.createBody(bDef);
		// оболочка тела (масса, пружность, импульс отпрынивания, шераховатость
		// (на сколько скользит))
		FixtureDef fDef = new FixtureDef();
		// shape - кружок или квадрат
		PolygonShape shape = new PolygonShape();
		// коробка 4 на 4
		shape.setAsBox(2,2);
		//
		fDef.shape = shape;
		// масса
		fDef.density = 2;
		// прыгать
		fDef.restitution = 0.5f;
		// едет по льду, скольжение
		fDef.friction = 0.1f;
		// создали квадрат
		rect.createFixture(fDef);
	}
	// стенки
	private void createWall(){
		BodyDef bDef = new BodyDef();
		bDef.type = BodyDef.BodyType.StaticBody;
		bDef.position.set(0, 0);

		Body w = world.createBody(bDef);

		FixtureDef fDef = new FixtureDef();
		ChainShape shape = new ChainShape();
		// точки
		shape.createChain(new Vector2[]{new Vector2(0,14),new Vector2(1,1), new Vector2(19,1)
				,new Vector2(20,14)});

		fDef.shape = shape;
		fDef.density = 2;
		fDef.friction = 0.1f;
		w.createFixture(fDef);
	}

}
