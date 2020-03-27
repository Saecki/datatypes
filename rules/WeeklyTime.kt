package bedbrains.shared.datatypes.rules

import bedbrains.platform.Time
import com.fasterxml.jackson.annotation.JsonProperty

class WeeklyTime {

    companion object {
        val MIN = WeeklyTime(0)
        val MAX = WeeklyTime(6, 23, 59, 59, 999)

        val firstDay: Int
            get() {
                return Time.getFirstWeekDay()
            }

        val now: WeeklyTime
            get() {
                return Time.currentWeeklyTime()
            }
    }

    @field:JsonProperty
    private var milliseconds = 0
        set(value) {
            field = Math.floorMod(value, 7 * 24 * 60 * 60 * 1000)
        }

    constructor(day: Int, hour: Int, minute: Int, second: Int, millis: Int) {
        milliseconds = day * 24 * 60 * 60 * 1000 + hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000 + millis
    }

    constructor(millis: Int) {
        milliseconds = millis
    }

    constructor()

    var day: Int
        get() = milliseconds / (24 * 60 * 60 * 1000)
        set(value) {
            milliseconds = value * 24 * 60 * 60 * 1000 + milliseconds % (24 * 60 * 60 * 1000)
        }

    var hour: Int
        get() = milliseconds % (24 * 60 * 60 * 1000) / (60 * 60 * 1000)
        set(value) {
            milliseconds = milliseconds - milliseconds % (24 * 60 * 60 * 1000) + value * 60 * 60 * 1000 + milliseconds % (60 * 60 * 1000)
        }

    var minute: Int
        get() = milliseconds % (60 * 60 * 1000) / (60 * 1000)
        set(value) {
            milliseconds = milliseconds - milliseconds % (60 * 60 * 1000) + value * 60 * 1000 + milliseconds % (60 * 1000)
        }

    var second: Int
        get() = milliseconds % (60 * 1000) / 1000
        set(value) {
            milliseconds = milliseconds - milliseconds % (60 * 1000) + value * 1000 + milliseconds % 1000
        }

    var millisecond: Int
        get() = milliseconds % 1000
        set(value) {
            milliseconds = milliseconds - milliseconds % 1000 + value
        }

    var localizedDay: Int
        set(value) {
            day = (firstDay + value) % 7
        }
        get() {
            return (7 - firstDay + day) % 7
        }

    val inDays: Double
        get() = milliseconds / (24.0 * 60.0 * 60.0 * 1000.0)

    val inHours: Double
        get() = milliseconds / (60.0 * 60.0 * 1000.0)

    val inMinutes: Double
        get() = milliseconds / (60.0 * 1000.0)

    val inSeconds: Double
        get() = milliseconds / 1000.0

    val inMilliseconds: Int
        get() = milliseconds

    val inDailyHours: Double
        get() = inDailyMilliseconds / (60.0 * 60.0 * 1000.0)

    val inDailyMinutes: Double
        get() = inDailyMilliseconds / (60.0 * 1000.0)

    val inDailySeconds: Double
        get() = inDailyMilliseconds / 1000.0

    val inDailyMilliseconds: Int
        get() = milliseconds - (milliseconds / (24 * 60 * 60 * 1000) * 24 * 60 * 60 * 1000)

    fun before(other: WeeklyTime): Boolean {
        return this.inMilliseconds < other.inMilliseconds
    }

    fun localizedBefore(other: WeeklyTime): Boolean {
        return this.localizedDay < other.localizedDay || this.localizedDay == other.localizedDay && this.inDailyMilliseconds > other.inDailyMilliseconds
    }

    fun after(other: WeeklyTime): Boolean {
        return this.inMilliseconds > other.inMilliseconds
    }

    fun localizedAfter(other: WeeklyTime): Boolean {
        return this.localizedDay > other.localizedDay || this.localizedDay == other.localizedDay && this.inDailyMilliseconds > other.inDailyMilliseconds
    }

    override fun equals(other: Any?): Boolean = when (other) {
        is WeeklyTime -> other.hashCode() == hashCode()
        else -> false
    }

    override fun hashCode(): Int {
        return inMilliseconds
    }
}