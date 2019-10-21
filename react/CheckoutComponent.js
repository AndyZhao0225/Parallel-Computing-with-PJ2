import React from "react";
import CheckoutList from "../../Container/Checkout/CheckoutListContainer";
import CartSummary from "../../Container/Cart/CartSummaryContainer";

export default function CheckoutComponent(props){   
    return(
        <div>
            <h2>Shopping Cart Using Redux</h2> 
            <p>User Name: {props.username}</p>
            <p>User Phone: {props.userphone}</p>
            <p>User Shipping Address: {props.useraddr}</p>
            {/* <p>Cart Amount {props.cartamount}</p> */}
            
           
            <CheckoutList/>
            <CartSummary/>

            <button onClick={() => props.pay(props.items, props.userid)} >
                Checkout
            </button>
        </div>
    )    
}