package com.oleksandrpriadko.domain.usecase

interface UseCase<Input, Output> {
    suspend operator fun invoke(input: Input): Output
}