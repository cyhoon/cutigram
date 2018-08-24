import React from "react";
import styled from "styled-components";

const LoginPage = () => {

    const Container = styled.div`
        display: flex;
        width: 800px;
        margin: 30px auto;
    `;

    const ImageContainer = styled.div`
        flex: 1;
    `;

    const LoginContainer = styled.div`
        flex: 1;
    `;

    //     width: 500px;
    //     height: 500px;
    //     background-image: url("https://www.instagram.com/static/images/homepage/screenshot1.jpg/d6bf0c928b5a.jpg");
    //     background-repeat: no-repeat;

    return (
        <Container>
            <ImageContainer><ImageContainer /></ImageContainer>
            <LoginContainer>Login</LoginContainer>
        </Container>
    );
};

export default LoginPage;