package com.tiagocc.tgame;

public abstract class GameComponent{
	protected boolean removed;
	public boolean isRemoved(){return removed;}
	protected boolean mInitialized = false;
	protected Game mGame;
	public GameObject gameObject;
	public void Initialize(GameObject go, Game game){
		this.gameObject = go;
		this.mGame=game;
		this.mInitialized = true;
		this.Awake();
	}
	public void Awake(){}
	public void Start(){}
	public void Update(){}
	public void onDestroy(){removed = true;}
	public void onWindowSizeChange(VectorInt wSize, int unitsPerTile){
	}

	public void onUpdateSize(Vector2 size){}
	public void onUpdatePosition(Vector2 position){}
	//public void onUpdateCameraPosition(Vector2 position){}
	//public void Draw(Canvas canvas){}
}
