package com.unacceptable.unacceptablelibrary;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.unacceptable.unacceptablelibrary.Adapters.IAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Screens.ExampleRecyclerViewTest;
import com.unacceptable.unacceptablelibrary.Screens.LoginActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowActivity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class AdapterTests {
    private NewAdapter m_Adapter;
    private IAdapterViewControl m_vControl;
    private RecyclerView m_RecyclerView;
    private RecyclerView.LayoutManager m_LayoutManager;

    private ActivityController<ExampleRecyclerViewTest> activityController;
    private ExampleRecyclerViewTest activity;

    @Before
    public void setUp() {
        //normal object testing setup
        m_vControl = mock(IAdapterViewControl.class);
        m_Adapter = new NewAdapter(0, 0, true, m_vControl);



        //attaching recyclerview to the fake activity
        /*activityController = Robolectric.buildActivity(LoginActivity.class);

        activityController.create().start().visible();
        activity = activityController.get();
        activity.setContentView(m_RecyclerView);*/
        /*activityController = Robolectric.buildActivity(ExampleRecyclerViewTest.class);
        activityController.create().start().visible();
        //ShadowActivity shadowActivity = shadowOf(activityController.get());
        activity = activityController.get();*/
        /*activity = Robolectric.setupActivity(ExampleRecyclerViewTest.class);

        //creating the recycler view
        //m_RecyclerView = (RecyclerView)findViewById(R.id.dailyLogList);
        m_RecyclerView = activity.findViewById(R.id.recyclerView);
        m_RecyclerView.setHasFixedSize(false);
        m_LayoutManager = new LinearLayoutManager(RuntimeEnvironment.systemContext);
        m_RecyclerView.setLayoutManager(m_LayoutManager);
        m_RecyclerView.setAdapter(m_Adapter);*/

    }

    @Test
    public void newAdapterWithoutAddEmpty_NoEmptyItem() {
        NewAdapter adapter = new NewAdapter(0, 0, false, m_vControl);
        Assert.assertEquals(0, adapter.size());
        Assert.assertEquals(0, adapter.Dataset().size());
    }

    @Test
    public void newAdapter_sizeReturnsZero() {
        Assert.assertEquals(0, m_Adapter.size());
        Assert.assertEquals("Empty", m_Adapter.get(0).name);
        Assert.assertEquals(1, m_Adapter.Dataset().size());
    }

    @Test
    public void addOneItem_sizeReturnsOneWithCorrectName() {
        ListableObject i = new ListableObject();
        i.name = "UnitTestObject";

        m_Adapter.add(i);

        Assert.assertEquals(1, m_Adapter.size());
        Assert.assertEquals("UnitTestObject", m_Adapter.get(0).name);
    }

    @Test
    public void addItemThenRemoveIt_sizeIsZeroWithEmpty() {
        ListableObject i = new ListableObject();
        i.name = "UnitTestObject";

        m_Adapter.add(i);

        Assert.assertEquals(1, m_Adapter.size());

        m_Adapter.remove(i);

        Assert.assertEquals(0, m_Adapter.size());
        //Assert.assertEquals("Empty", m_Adapter.Dataset().get(0).name);
    }

    /*@Test
    public void addItemThenClickIt() {
        ListableObject i = new ListableObject();
        i.name = "UnitTest";

        m_Adapter.add(i);

        m_RecyclerView.getChildAt(0).performClick();

        verify(m_vControl).onItemClick(any(View.class), i);
    }*/
}
