import {onMounted, onUnmounted, ref} from "vue"


export function useWindowFocusHook() {
    const isWindowFocus = ref(true)

    const handleFocus = () => {
        isWindowFocus.value = true
    }

    const handleBlur = () => {
        isWindowFocus.value = false
    }
    onMounted(() => {
        window.addEventListener('focus', handleFocus)
        window.addEventListener("blur", handleBlur)

    })

    onUnmounted(() => {
        window.removeEventListener("focus", handleFocus)
        window.removeEventListener("blur", handleBlur)
    })

    return {
        isWindowFocus
    }
}

