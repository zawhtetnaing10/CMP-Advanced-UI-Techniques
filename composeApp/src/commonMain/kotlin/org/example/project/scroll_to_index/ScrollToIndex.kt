package org.example.project.scroll_to_index

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ScrollToIndexDemo() {

    val verticalScrollState = rememberLazyListState()
    val horizontalScrollState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    var selectedTabIndex by remember {mutableStateOf(0)}
    var selectedItemIndex by remember { mutableStateOf(0) }

    Scaffold { paddingValues ->
        LazyColumn(
            state = verticalScrollState,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(
                bottom = paddingValues.calculateBottomPadding(),
                start = paddingValues.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                end = paddingValues.calculateEndPadding(layoutDirection = LayoutDirection.Ltr)
            )
        ) {

            stickyHeader {
                Surface(color = MaterialTheme.colorScheme.primary) {
                    LazyRow(
                        state = horizontalScrollState,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed((1..10).toList()) { index, num ->

                            // Calculate text width
                            var textWidth by remember { mutableStateOf(0) }
                            val density = LocalDensity.current

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(top = 40.dp)
                            ) {
                                Text(
                                    "Scroll to item $num", color = Color.White,
                                    modifier = Modifier
                                        .onGloballyPositioned {
                                            textWidth = it.size.width
                                        }
                                        .clickable {
                                            coroutineScope.launch {
                                                selectedItemIndex = index
                                                selectedTabIndex = index
                                                horizontalScrollState.animateScrollToItem(selectedTabIndex)
                                                verticalScrollState.animateScrollToItem(selectedItemIndex)
                                            }
                                        })

                                Spacer(modifier = Modifier.height(12.dp))

                                if (selectedItemIndex == index) {
                                    Surface(
                                        color = Color.White,
                                        modifier = Modifier.height(4.dp)
                                            .width(with(density) { textWidth.toDp() })
                                    ) { }
                                }
                            }
                        }
                    }
                }
            }

            items((1..10).toList()) { num ->
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text("Category - $num", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    (1..5).forEach { _ ->
                        Text("Menu Item", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ScrollToIndexDemoPreview() {
    ScrollToIndexDemo()
}