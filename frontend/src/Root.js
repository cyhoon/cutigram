import React from "react";

import { BrowserRouter, Route, Switch } from "react-router-dom";

import { LoginPage, RegisterPage } from "./pages/Auth";

const Root = () => {
    return (
        <div>
            <BrowserRouter>
                <Switch>
                    <Route path="/auth/login" component={LoginPage} />
                    <Route path="/auth/register" component={RegisterPage} />
                </Switch>
            </BrowserRouter>
        </div>
    );
};

export default Root;