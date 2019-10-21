import React from "react";
import CartItem from "../../container/Cart/CartItemContainer";

export default function CartList(props) { 
    let {items} = props;
    console.log("CartList function render");
    return (
        <div> 
        <h2>Cart List</h2>
        <table>
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Qty</th>
                    <th>Total</th>
                </tr>
            </thead>
            <tbody>
            {
                items.map (item => (
                    <tr>
                        <td>{item.name}</td>
                        <td>$ {item.price}</td>
                        <td>{item.qty}</td>
                        <td>$ {item.price * item.qty}</td>
                    </tr>
                ))
            }
            </tbody>
        </table>
        </div>
     )
}