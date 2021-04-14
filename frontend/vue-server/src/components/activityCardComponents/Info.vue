<template>
    <div id="container">
        <h1>{{ name }}</h1>
        <p>{{ description }}</p>
        <h3>Informasjon:</h3>
        <ul>
            <li>Kategori:</li>
            <li>Sted:</li>
            <li>Tid:</li>
            <li>Varighet:</li>
            <li>Værmelding:</li>
            <li>Deltakere:</li>
        </ul>
        <ul>
            <li>{{ type }}</li>
            <li>{{ place }}</li>
            <li>{{ time }}</li>
            <li>{{ duration }}</li>
            <li>{{ weather }}</li>
            <li>{{ participants }}</li>
        </ul>
        <button>{{ status }}</button>
    </div>
</template>
<script>
    export default {
        name: "Info",

        props: {
            activity: null,
        },

        data() {
            return {
                name: "",
                description: "",
                type: "",
                place: "",
                time: "",
                duration: "",
                weather: "",
                participants: "",
                status: "",
            }
        },

        methods: {
            load() {
                
            }
        }
    }
</script>