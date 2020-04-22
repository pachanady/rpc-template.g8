package com.bigcommerce.common.command

// Knows how to apply(overlap) command data on top of domain object(entity)
trait UpdateCommand[T] {
  def update(entity: T): T
}
