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

describe('ReturnItem e2e test', () => {
  const returnItemPageUrl = '/return-item';
  let username: string;
  let password: string;
  const returnItemSample = { returnQuantity: 22757.74 };

  let returnItem;
  let returnHeader;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/return-headers',
      body: { needsInventoryReceive: false, supplierRmaId: 'indeed' },
    }).then(({ body }) => {
      returnHeader = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/return-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/return-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/return-items/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/return-headers', {
      statusCode: 200,
      body: [returnHeader],
    });

    cy.intercept('GET', '/api/return-header-types', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/order-items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (returnItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/return-items/${returnItem.id}`,
      }).then(() => {
        returnItem = undefined;
      });
    }
  });

  afterEach(() => {
    if (returnHeader) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/return-headers/${returnHeader.id}`,
      }).then(() => {
        returnHeader = undefined;
      });
    }
  });

  it('ReturnItems menu should load ReturnItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('return-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReturnItem').should('exist');
    cy.location('pathname').should('eq', returnItemPageUrl);
  });

  describe('ReturnItem page', () => {
    it('should have translated page title', () => {
      cy.visit(returnItemPageUrl);
      cy.getEntityHeading('ReturnItem').should('not.contain', 'snpApp.returnItem.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(returnItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReturnItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${returnItemPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ReturnItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnItemPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/return-items',
          body: {
            ...returnItemSample,
            returnHeader,
          },
        }).then(({ body }) => {
          returnItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/return-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/return-items?page=0&size=20>; rel="last",<http://localhost/api/return-items?page=0&size=20>; rel="first"',
              },
              body: [returnItem],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(returnItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReturnItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('returnItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnItemPageUrl);
      });

      it('edit button click should load edit ReturnItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReturnItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnItemPageUrl);
      });

      it('edit button click should load edit ReturnItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReturnItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnItemPageUrl);
      });

      it('last delete button click should delete instance of ReturnItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('returnItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnItemPageUrl);

        returnItem = undefined;
      });
    });
  });

  describe('new ReturnItem page', () => {
    beforeEach(() => {
      cy.visit(returnItemPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ReturnItem');
    });

    it('should create an instance of ReturnItem', () => {
      cy.get(`[data-cy="sequenceNo"]`).type('29318');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '29318');

      cy.get(`[data-cy="description"]`).type('mmm onto inasmuch');
      cy.get(`[data-cy="description"]`).should('have.value', 'mmm onto inasmuch');

      cy.get(`[data-cy="returnQuantity"]`).type('4823.19');
      cy.get(`[data-cy="returnQuantity"]`).should('have.value', '4823.19');

      cy.get(`[data-cy="receivedQuantity"]`).type('23263.12');
      cy.get(`[data-cy="receivedQuantity"]`).should('have.value', '23263.12');

      cy.get(`[data-cy="returnPrice"]`).type('15856.47');
      cy.get(`[data-cy="returnPrice"]`).should('have.value', '15856.47');

      cy.get(`[data-cy="returnItemPurpose"]`).type('oddball joyous excluding');
      cy.get(`[data-cy="returnItemPurpose"]`).should('have.value', 'oddball joyous excluding');

      cy.get(`[data-cy="returnHeader"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        returnItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', returnItemPageUrl);
    });
  });
});
