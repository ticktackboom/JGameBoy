import com.meadowsapps.jgameboy.groovy.core.util.Register16Bit
import com.meadowsapps.jgameboy.groovy.core.util.UInt16

/**
 * Created by dmeadows on 3/2/2017.
 */

UInt16 test = new UInt16(0xFFFF)
Register16Bit r = new Register16Bit()
r.word = test
println(r)
test += 1
println(r)