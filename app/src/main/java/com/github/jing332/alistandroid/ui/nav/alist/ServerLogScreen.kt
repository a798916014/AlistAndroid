package com.github.jing332.alistandroid.ui.nav.alist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.jing332.alistandroid.R
import com.github.jing332.alistandroid.constant.LogLevel
import com.github.jing332.alistandroid.constant.LogLevel.Companion.toLevelString
import com.github.jing332.alistandroid.data.appDb

@Composable
fun ServerLogScreen(modifier: Modifier) {
    val list by appDb.serverLogDao.flowAll().collectAsState(initial = emptyList())

    var showDescDialog by remember { mutableStateOf<String?>(null) }
    if (showDescDialog != null)
        AlertDialog(
            onDismissRequest = { showDescDialog = null },
            title = { Text(stringResource(R.string.description)) },
            text = { Text(showDescDialog.toString()) },
            confirmButton = {
                TextButton(
                    onClick = { showDescDialog = null }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )

    ElevatedCard(modifier.padding(bottom = 8.dp)) {
        Column {
            Text(
                text = stringResource(R.string.log),
                Modifier
                    .align(Alignment.Start)
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )

            LazyColumn(
                Modifier.padding(8.dp),
            ) {
                val lastItemId = list.lastOrNull()?.id
                items(list, { it.id }) {
                    Column(Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple()
                    ) {
                        showDescDialog = it.level.toLevelString()
                    }
                    ) {
                        SelectionContainer {
                            Text(
                                it.message,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                color = when (it.level) {
                                    LogLevel.DEBUG -> MaterialTheme.colorScheme.onBackground.copy(
                                        alpha = 0.5f
                                    )

                                    LogLevel.INFO -> MaterialTheme.colorScheme.onBackground.copy(
                                        alpha = 0.8f
                                    )

                                    LogLevel.WARN -> MaterialTheme.colorScheme.onBackground
                                    LogLevel.ERROR -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.primary
                                }
                            )
                        }

                        if (lastItemId != it.id)
                            HorizontalDivider(thickness = 0.8.dp)
                    }
                }
            }
        }
    }

}