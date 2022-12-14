package helper;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TileMapHelper {

    private TiledMap tiledmap;

    public TileMapHelper(){

    }

    public OrthogonalTiledMapRenderer setupMap(){
        tiledmap = new TmxMapLoader().load("map/map.tmx");
        return new OrthogonalTiledMapRenderer(tiledmap);
    }
}
