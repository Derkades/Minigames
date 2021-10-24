<template>
    <!-- <img  :alt="name"> -->
    <div class="bg-gray-800 rounded-lg shadow-lg w-full p-6 space-y-6 text-white">
        <div class="w-full h-4">
            <p class="text-xl font-bold">{{ playerdata.name }}</p>
        </div>
        <div class="w-full h-auto flex space-x-6">
            <div class="">
                <img class="flex h-auto m-auto" :src="'https://crafatar.com/renders/body/' + playerdata.uuid" alt="photo">
            </div>
            <div class="w-auto text-left">
                <p>Points: {{ playerdata.points }}</p>
                <p>Is staff: {{ playerdata.has_debug_perm }}</p>
                <p>World: {{ playerdata.world }}</p>
                <p>Game mode: {{ playerdata.game_mode }}</p>
            </div>
        </div>
    </div>
</template>

<script>
export default {
  name: 'OnlinePlayer',
  props: {
    playerdata: Object
  }
}
</script>
