<template>
    <div id="container">
        <h1>{{ name }}</h1>
        <img :src="ownerImage">
        <h3>{{ ownerName }}</h3>
        <p>{{ time }}</p>
        <p>Kategori: {{ type }}</p>
        <p>Sted: {{ location }}</p>
        <p>Deltakere: {{ currentParticipants }} / {{ totalParticipants }}</p>
        <div>
            <img v-for="image in images" :key="image.url" :src="image.url">
        </div>
        <button>{{ status }}</button>
    </div>
</template>
<script>
    export default {
        name: "Activity",

        data() {
            return {
                name: "",
                ownerImage: "",
                ownerName: "",
                time: null,
                type: "",
                location: "",
                currentParticipants: 0,
                totalParticipants: 0,
                images: {},
                status: "",
            }
        },
    }
</script>