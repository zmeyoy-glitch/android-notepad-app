package com.example.notepad.data

/**
 * Модель временного слота (15 минут)
 */
data class TimeSlot(
    val slotNumber: Int,      // Номер слота (1-12)
    val startTime: String,    // Начало слота "HH:mm"
    val endTime: String       // Конец слота "HH:mm"
) {
    companion object {
        /**
         * Генерация всех временных слотов с 17:00 до 20:00 (шаг 15 минут)
         */
        fun generateAllSlots(): List<TimeSlot> {
            val slots = mutableListOf<TimeSlot>()
            
            var currentTimeHour = 17
            var currentTimeMinute = 0
            
            for (i in 1..12) {
                val startHour = currentTimeHour.toString().padStart(2, '0')
                val startMinute = currentTimeMinute.toString().padStart(2, '0')
                
                // Добавляем 15 минут к текущему времени
                currentTimeMinute += 15
                
                if (currentTimeMinute >= 60) {
                    currentTimeHour++
                    currentTimeMinute -= 60
                }
                
                val endHour = currentTimeHour.toString().padStart(2, '0')
                val endMinute = currentTimeMinute.toString().padStart(2, '0')
                
                slots.add(TimeSlot(i, "$startHour$startMinute", "$endHour$endMinute"))
            }
            
            return slots
        }
        
        /**
         * Получение слота по номеру (1-12)
         */
        fun getSlotByNumber(slotNumber: Int): TimeSlot? {
            val allSlots = generateAllSlots()
            return allSlots.find { it.slotNumber == slotNumber }
        }
    }
}

/**
 * Перечисление для отображения временных слотов в UI
 */
enum class TimeSlotDisplay(val displayName: String) {
    SLOT_1("17:00 - 17:15"),
    SLOT_2("17:15 - 17:30"),
    SLOT_3("17:30 - 17:45"),
    SLOT_4("17:45 - 18:00"),
    SLOT_5("18:00 - 18:15"),
    SLOT_6("18:15 - 18:30"),
    SLOT_7("18:30 - 18:45"),
    SLOT_8("18:45 - 19:00"),
    SLOT_9("19:00 - 19:15"),
    SLOT_10("19:15 - 19:30"),
    SLOT_11("19:30 - 19:45"),
    SLOT_12("19:45 - 20:00")
}

fun Int.toDisplayString(): String {
    return when (this) {
        1 -> "17:00 - 17:15"
        2 -> "17:15 - 17:30"
        3 -> "17:30 - 17:45"
        4 -> "17:45 - 18:00"
        5 -> "18:00 - 18:15"
        6 -> "18:15 - 18:30"
        7 -> "18:30 - 18:45"
        8 -> "18:45 - 19:00"
        9 -> "19:00 - 19:15"
        10 -> "19:15 - 19:30"
        11 -> "19:30 - 19:45"
        12 -> "19:45 - 20:00"
        else -> ""
    }
}