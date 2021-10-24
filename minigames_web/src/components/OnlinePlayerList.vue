<template>
    <p class="text-2xl text-left">Online players</p>
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <div v-for='player in players' :key="player.uuid">
        <!-- <OnlinePlayer :uuid="player.uuid" :name="player.name" :points="player.points" /> -->
        <OnlinePlayer :playerdata="player" />
      </div>
        <!-- <OnlinePlayer uuid="09948878-fe20-44e3-a072-42c39869dd1f" name="Derkades" points="2" /> -->
        <!-- <OnlinePlayer uuid="8a043247-5cc1-4392-a9ea-e9054dc1f99c" name="Sneewie"  points="2" /> -->
    </div>
</template>

<script>
import OnlinePlayer from './OnlinePlayer'

export default {
  name: 'OnlinePlayerList',
  components: {
    OnlinePlayer
  },
  props: {
    players: Array
  },
}
</script>
