package com.namello.app

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class SessionWidgetProvider : AppWidgetProvider() {
    private val ny = ZoneId.of("America/New_York")
    private val london = ZoneId.of("Europe/London")
    private val tokyo = ZoneId.of("Asia/Tokyo")
    private val sydney = ZoneId.of("Australia/Sydney")
    private val timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss")

    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        ids.forEach { update(context, manager, it) }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        updateAll(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == Intent.ACTION_TIME_TICK || intent.action == Intent.ACTION_TIMEZONE_CHANGED) updateAll(context)
    }

    private fun updateAll(context: Context) {
        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(ComponentName(context, SessionWidgetProvider::class.java))
        ids.forEach { update(context, manager, it) }
    }

    private fun update(context: Context, manager: AppWidgetManager, id: Int) {
        val now = ZonedDateTime.now(ny)
        val lines = listOf(
            sessionLine("Sydney", sydney, now),
            sessionLine("Tokyo", tokyo, now),
            sessionLine("London", london, now),
            sessionLine("New York", ny, now)
        )
        val active = lines.filter { it.second }.joinToString("  ·  ") { it.first }
        val text = if (active.isBlank()) "هیچ سشنی فعال نیست" else "فعال: $active"
        val views = RemoteViews(context.packageName, R.layout.widget_sessions)
        views.setTextViewText(R.id.widgetClock, now.format(timeFmt) + " NY")
        views.setTextViewText(R.id.widgetSessions, text + "\n" + lines.joinToString("\n") { it.first + "  " + it.third })
        views.setOnClickPendingIntent(R.id.widgetRoot, PendingIntent.getActivity(context, id, Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
        manager.updateAppWidget(id, views)
    }

    private fun sessionLine(name: String, zone: ZoneId, nowNy: ZonedDateTime): Triple<String, Boolean, String> {
        val now = nowNy.withZoneSameInstant(zone)
        val (start, end) = when (name) {
            "Sydney" -> LocalTime.of(22, 0) to LocalTime.of(7, 0)
            "Tokyo" -> LocalTime.of(0, 0) to LocalTime.of(9, 0)
            "London" -> LocalTime.of(8, 0) to LocalTime.of(17, 0)
            else -> LocalTime.of(8, 0) to LocalTime.of(17, 0)
        }
        val t = now.toLocalTime()
        val active = if (start < end) !t.isBefore(start) && t.isBefore(end) else !t.isBefore(start) || t.isBefore(end)
        return Triple(name, active, if (active) "●" else "○")
    }
}
