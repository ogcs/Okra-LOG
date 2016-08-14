/**
 *
 */
class OkraComponent {


    constructor() {
    }

    static min(a, b, callback = undefined) {
        if (callback)
            callback(Math.min(a, b));
        else
            console.log("No callback.")
    }
}