import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

const TOKEN_KEY = 'monster_token'
const USER_KEY = 'monster_username'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem(TOKEN_KEY) || '')
  const username = ref(localStorage.getItem(USER_KEY) || '')

  const isLoggedIn = computed(() => Boolean(token.value))

  function setSession(nextToken: string, nextUsername: string) {
    token.value = nextToken
    username.value = nextUsername
    localStorage.setItem(TOKEN_KEY, nextToken)
    localStorage.setItem(USER_KEY, nextUsername)
  }

  function clearSession() {
    token.value = ''
    username.value = ''
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
  }

  return { token, username, isLoggedIn, setSession, clearSession }
})
