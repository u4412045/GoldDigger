package com.golddigger.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.GenericServer;
import com.golddigger.client.TestingClient;
import com.golddigger.model.Player;
import com.golddigger.services.MoveService.Direction;
import com.golddigger.templates.TestGameTemplate;

public class NextGameTest {
	GenericServer server;
	TestingClient client;
	private static final String MAP_1 = "wwwww\nw.2.w\nw.b.w\nw...ww\nwwwww";
	private static final String MAP_2 = "www\nwbw\nwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		server = new GenericServer();
		server.add(new TestGameTemplate(MAP_1));
		server.add(new TestGameTemplate(MAP_2));
		server.add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
	}

	@After()
	public void halt(){
		server.stop();
	}
	
	@Test
	public void test() {
		assertEquals("Should be on the first map", ".2.\n.b.\n...", client.view().trim());
		assertEquals("Still gold on the field","FAILED", client.next().trim());
		client.move(Direction.NORTH);
		client.grab();
		assertEquals("Still holding the gold","FAILED", client.next().trim());
		client.move(Direction.SOUTH);
		client.drop();
		assertEquals("Should have been allowed to progress","OK", client.next().trim());
		assertEquals("Should be on the next map", "www\nwbw\nwww", client.view().trim());
		
	}

}
