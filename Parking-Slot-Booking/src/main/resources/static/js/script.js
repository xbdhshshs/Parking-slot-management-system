const paymentStart = () => {
    var amount; //=  $("#outTime").val()-$("#inTime").val() + $("#days").val();
    var time= $("#outTime").val() - $("#inTime").val();
    let desc, xyz;
    if(time>4){
        amount=$("#outTime").val() - $("#inTime").val();
        amount=amount*2500;
        desc="Minimum payment: 100";
        xyz = "Parking charges:"+ (amount/100-100) ;
    }
    else{
        amount=$("#outTime").val() - $("#inTime").val();
        amount=amount*2500;
        xyz="Minimum payment: 100";
        desc = "Amount refunded:"+(100-amount/100) +" Final amount:"+(amount/100);
    }
    var options = {
        "key": "rzp_test_haDRsJIQo9vFPJ",
        "amount": amount, // Example: 2000 paise = INR 20
        "name": xyz,
        "description": desc,
        "image": "",// COMPANY LOGO
        "handler": function (response) {
            console.log(response);
            // AFTER TRANSACTION IS COMPLETE YOU WILL GET THE RESPONSE HERE.
        },
        "prefill": {
            "name": "", // pass customer name
            "email": '',// customer email
            "contact": '' //customer phone no.
        },
        "notes": {
            "address": "address" //customer address
        },
        "theme": {
            "color": "#15b8f3" // screen color
        }
    };
    console.log(options);
    var propay = new Razorpay(options);
    propay.open();
    console.log("payment started..");
    var amount = $("#payment_field").val();
    console.log(amount);
    if (amount == "" || amount == null) {
        // alert("amount is required !!");
        swal("Failed !!", "amount is required !!", "error");
        return;
    }

    //coded...
    // we will use ajax to send request to server to create order- jquery

    $.ajax({
        url: "/admin/create_order",
        data: JSON.stringify({ amount: amount, info: "order_request" }),
        contentType: "application/json",
        type: "POST",
        dataType: "json",
        success: function (response) {
            //invoked when success
            console.log(response);
            if (response.status == "created") {
                //open payment form
                let options = {
                    key: "rzp_test_3fGEPJTbBw4c9f",
                    amount: response.amount,
                    currency: "INR",
                    name: "Car Parking",
                    description: "Payment",
                    order_id: response.id,
                    handler: function (response) {
                        console.log(response.razorpay_payment_id);
                        console.log(response.razorpay_order_id);
                        console.log(response.razorpay_signature);
                        console.log("payment successful !!");
                        swal("Good job!", "congrates !! Payment successful !!", "success");
                    },
                    prefill: {
                        name: "",
                        email: "",
                        contact: "",
                    },

                    notes: {
                        address: "OOP project ",
                    },
                    theme: {
                        color: "#DA0037",
                    },
                };

                let rzp = new Razorpay(options);

                rzp.on("payment.failed", function (response) {
                    console.log(response.error.code);
                    console.log(response.error.description);
                    console.log(response.error.source);
                    console.log(response.error.step);
                    console.log(response.error.reason);
                    console.log(response.error.metadata.order_id);
                    console.log(response.error.metadata.payment_id);
                    //alert("Oops payment failed !!");
                    swal("Failed !!", "Oops payment failed !!", "error");
                });

                rzp.open();
            }
        },
        error: function (error) {
            //invoked when error
            console.log(error);

        },
    });
};