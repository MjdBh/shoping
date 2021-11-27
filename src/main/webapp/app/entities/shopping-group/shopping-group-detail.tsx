import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './shopping-group.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ShoppingGroupDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const shoppingGroupEntity = useAppSelector(state => state.shoppingGroup.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="shoppingGroupDetailsHeading">
          <Translate contentKey="shoppingApp.shoppingGroup.detail.title">ShoppingGroup</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{shoppingGroupEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="shoppingApp.shoppingGroup.name">Name</Translate>
            </span>
          </dt>
          <dd>{shoppingGroupEntity.name}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="shoppingApp.shoppingGroup.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {shoppingGroupEntity.createdAt ? (
              <TextFormat value={shoppingGroupEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="shoppingApp.shoppingGroup.createdBy">Created By</Translate>
          </dt>
          <dd>{shoppingGroupEntity.createdBy ? shoppingGroupEntity.createdBy.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/shopping-group" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/shopping-group/${shoppingGroupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ShoppingGroupDetail;
