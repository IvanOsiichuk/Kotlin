/** Користувач
 * @param _username Ім'я
 * @param paidDays кількість оплачених днів */
class User (_username: String, paidDays: UInt = 0u){
    enum class PlanType { FREE, PAID }

    // ВЛАСТИВОСТІ
    var username: String = "default"
        set(value) {
            if (!value.isBlank() && value.all { c -> c.isLetterOrDigit() || c == ' ' }) // !maybe regex
                field = value
        }
        init {
            username = _username
        }

    var xp = 0u
        set(value) {
            field = value
            if (field >= 500u) {
                field -= 500u
                level++
            }
        }

    var plan = PlanType.FREE // платний чи безкоштовний обліковий запис
        private set         // альтернативно можна використовувати true/false
    init {
        plan = if (paidDays > 0u) PlanType.PAID else PlanType.FREE
    }

    var level = 0u
        private set

    var actions = 0u // кількість дій за день
        private set

    var paidDays = 0u
        private set

    var lastAction = "none"
        private set

    // ВЛАСТИВОСТІ ЗІ СТАНДАРТНИМИ get & set
    private var freeLimit = 3u // кількість можливих дій безкоштовного користувача

    // МЕТОДИ
    /** Виконати певну дію */
    fun Action(action: String): Boolean {
        if ((plan == PlanType.FREE && actions < freeLimit) ||
            (plan == PlanType.PAID && paidDays > 0u)) {
            xp += 100u
            actions++
            lastAction = action
            return true
        }
        return false
    }

    /** Закінчити день */
    fun finishDay() { // альтернативно можна реалізувати Real-time систему
        actions = 0u
        if (plan == PlanType.PAID)
            paidDays++
    }
}
