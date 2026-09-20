import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('OrderItemType e2e test', () => {
  const orderItemTypePageUrl = '/order-item-type';
  let username: string;
  let password: string;
  const orderItemTypeSample = { name: 'colon blah forsaken' };

  let orderItemType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/order-item-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-item-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-item-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (orderItemType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-item-types/${orderItemType.id}`,
      }).then(() => {
        orderItemType = undefined;
      });
    }
  });

  it('OrderItemTypes menu should load OrderItemTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-item-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderItemType').should('exist');
    cy.location('pathname').should('eq', orderItemTypePageUrl);
  });

  describe('OrderItemType page', () => {
    it('should have translated page title', () => {
      cy.visit(orderItemTypePageUrl);
      cy.getEntityHeading('OrderItemType').should('not.contain', 'snpApp.orderItemType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderItemTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderItemType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${orderItemTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('OrderItemType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-item-types',
          body: orderItemTypeSample,
        }).then(({ body }) => {
          orderItemType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-item-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [orderItemType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderItemTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OrderItemType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderItemType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemTypePageUrl);
      });

      it('edit button click should load edit OrderItemType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderItemType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemTypePageUrl);
      });

      it('edit button click should load edit OrderItemType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderItemType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemTypePageUrl);
      });

      it('last delete button click should delete instance of OrderItemType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orderItemType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemTypePageUrl);

        orderItemType = undefined;
      });
    });
  });

  describe('new OrderItemType page', () => {
    beforeEach(() => {
      cy.visit(orderItemTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderItemType');
    });

    it('should create an instance of OrderItemType', () => {
      cy.get(`[data-cy="name"]`).type('cleverly gosh');
      cy.get(`[data-cy="name"]`).should('have.value', 'cleverly gosh');

      cy.get(`[data-cy="description"]`).type('why');
      cy.get(`[data-cy="description"]`).should('have.value', 'why');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        orderItemType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', orderItemTypePageUrl);
    });
  });
});
