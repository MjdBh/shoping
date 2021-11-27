import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './item.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ItemDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const itemEntity = useAppSelector(state => state.item.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="itemDetailsHeading">
          <Translate contentKey="shoppingApp.item.detail.title">Item</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{itemEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="shoppingApp.item.name">Name</Translate>
            </span>
          </dt>
          <dd>{itemEntity.name}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="shoppingApp.item.price">Price</Translate>
            </span>
          </dt>
          <dd>{itemEntity.price}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="shoppingApp.item.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{itemEntity.createdAt ? <TextFormat value={itemEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="picture">
              <Translate contentKey="shoppingApp.item.picture">Picture</Translate>
            </span>
          </dt>
          <dd>{itemEntity.picture}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="shoppingApp.item.state">State</Translate>
            </span>
          </dt>
          <dd>{itemEntity.state}</dd>
          <dt>
            <Translate contentKey="shoppingApp.item.group">Group</Translate>
          </dt>
          <dd>{itemEntity.group ? itemEntity.group.id : ''}</dd>
          <dt>
            <Translate contentKey="shoppingApp.item.owner">Owner</Translate>
          </dt>
          <dd>{itemEntity.owner ? itemEntity.owner.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/item/${itemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ItemDetail;
