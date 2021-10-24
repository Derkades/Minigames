<template>
  <!-- <img alt="Vue logo" src="./assets/logo.png"> -->
  <div class="text-left p-6">
    Game state: {{ apiResponse.game_state.name.toLowerCase() }}
  </div>
  <div class="p-6">
    <OnlinePlayerList :players="apiResponse.online_players"/>
  </div>
  <!-- <HelloWorld msg="Welcome to Your Vue.js App" /> -->
</template>

<script>
// import HelloWorld from './components/HelloWorld.vue'
import OnlinePlayerList from './components/OnlinePlayerList.vue'
import { onMounted, ref, inject } from 'vue'

export default {
  name: 'App',
  components: {
    // HelloWorld,
    OnlinePlayerList
  },
  setup() {
    const axios = inject('axios')  // inject axios

    const apiResponse = ref([]);

    setInterval(() => {
      axios.get('http://10.0.1.1:26495/')
        .then((response) => {
          apiResponse.value = response.data;
        });
    }, 500);

    return { apiResponse }
  },
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
