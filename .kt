class ArithmeticEncoder(val low: Float, val high: Float, val range: Float) {
    private var low_ = low
    private var high_ = high
    private var range_ = range

    fun encode(symbol: Int, frequencyTable: FrequencyTable) {
        val range = high_ - low_
        val cumFreq = frequencyTable.getCumulativeFrequency(symbol)

        high_ = low_ + range * cumFreq.high / frequencyTable.getTotal()
        low_ += range * cumFreq.low / frequencyTable.getTotal()

        while (true) {
            if (high_ < 0.5f) {
                writeBit(0)
                for (i in 0 until numUnderflowBits) {
                    writeBit(1)
                }
                numUnderflowBits = 0
            } else if (low_ >= 0.5f) {
                writeBit(1)
                for (i in 0 until numUnderflowBits) {
                    writeBit(0)
                }
                numUnderflowBits = 0
            } else if (low_ >= 0.25f && high_ < 0.75f) {
                numUnderflowBits++
                low_ = 2 * (low_ - 0.25f)
                high_ = 2 * (high_ - 0.25f)
            } else {
                break
            }
            low_ = 2 * low_
            high_ = 2 * high_ + 1
        }
    }

    private fun writeBit(bit: Int) {
        output.write(bit)
    }
}
