<template>
    <div id="container">
        <Info></Info>
        <Map></Map>
        <Equipments></Equipments>
        <Chat></Chat>
    </div>
</template>
<script>
    import Info from './Info.vue'
    import Map from './Map.vue'
    import Equipments from './Equipments.vue'
    import Chat from './Chat.vue'

    export default {
        name: "ActivityCard",
        components: {
            Info,
            Map,
            Equipments,
            Chat
        },

        props: {
            activity: {
                type: Activity,
                required: true
            },
        },

        data() {
            return {
                activity: null,
            }
        }
    }
</script>